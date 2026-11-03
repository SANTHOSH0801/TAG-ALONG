package com.tagalong.service;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tagalong.repository.LoginLogRepository;
import com.tagalong.repository.UserRepository;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.security.auth.login.AccountLockedException;

import com.tagalong.Config.AuthConfiguration;
import com.tagalong.Constants.Role;
import com.tagalong.dto.AuthRequest;
import com.tagalong.entity.User;
import com.tagalong.exception.InvalidCredentialsException;
import com.tagalong.modal.LoginLog;
import com.tagalong.modal.LoginLog.LockStatus;
import com.tagalong.util.BasicValidator;
import com.tagalong.util.PassworHashdHelper;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService2 {


    private final UserRepository userRepository;
    private final LoginLogRepository loginLogRepo;
    private final AuthConfiguration authConfiguration;

    public Pair<Boolean, Integer> validateLoginLock(
        HttpHeaders headers, User user, String deviceUuid, LoginLog lastLoginLog
    ) throws Exception {
        Pair<Boolean, Integer> lockUser = updateLoginAttempt(user, headers, lastLoginLog, deviceUuid);
        return lockUser;
    }

    public Pair<Boolean, Integer> lockUserAccount(String userUid, LoginLog.LoginStatus status)
        throws AuthenticationException {
            Instant cutOffTime = Instant.now().minus(Duration.ofMinutes(authConfiguration.getFailedAttemptCheckTimeInMin()));

            int failedAttemptCount;
            if (status == LoginLog.LoginStatus.FAILED) {
                failedAttemptCount = loginLogRepo.consecutiveFailureCountForIncorrectPassword(userUid, cutOffTime.toEpochMilli());
            } else if (status == LoginLog.LoginStatus.INCORRECT_OTP) {
                failedAttemptCount = loginLogRepo.consecutiveFailureCountForIncorrectOtp(userUid, cutOffTime.toEpochMilli());
            } else {
                throw new AuthenticationException("No unusual authentication event to track");
            }
            failedAttemptCount += 1;
            int maxAllowedWrongAttempt = authConfiguration.getMaxAllowedWrongAttempt();
            boolean lockUser = false;
            int retriesLeft = maxAllowedWrongAttempt - failedAttemptCount;
            if (retriesLeft <= 0) {
                lockUser = true;
            }
            return Pair.of(lockUser, retriesLeft);
        }

    private User findPasswordMatchingUser(List<User> users, String password){
        if(StringUtils.isBlank(password)){
            return null;
        }

        for (User u: users){
            try{
                if(PassworHashdHelper.matches(password, u.getPassword())){
                    return u;
                }
            } catch (Exception ignored) {
    
            }
        }
        return null;
    }

    public Pair<Boolean, Integer> updateLoginAttempt(
        User user, 
        HttpHeaders headers, 
        LoginLog lastLoginLog,
        String deviceUuid
    ) throws Exception {
        LoginLog.LockStatus lockStatus = LoginLog.LockStatus.OPEN;
        saveLoginLock(user.getUid(), headers, lockStatus, deviceUuid);
        
        Pair<Boolean, Integer> lockUser = lockUserAccount(user.getUid(), LoginLog.LoginStatus.FAILED);

        log.debug("DEBUG updateLoginAttempt: lockUser.left={}, retry count={}", lockUser.getLeft(), lockUser.getRight());

        if (lockUser.getLeft()) {
            lockStatus = LoginLog.LockStatus.LOCKED;

            saveLoginLock(user.getUid(), headers, lockStatus, deviceUuid);
            Instant lockExpTime = getLockExpTime(
                lastLoginLog.getCreatedAt(),
                user.getRole()
            );
            // Needs to do
            // emailUtil.sendAccountLockedEmail(user, lockExpTime);
        }
        return lockUser;
    }

    public Instant getLockExpTime(Instant userLockedTime, Role userRole) {

        Integer timeToResetWrongAttemptCountInMin = 
            authConfiguration.getTimeToResetWrongAttemptCountInMin(userRole);

        return userLockedTime.plus(Duration.ofMinutes(timeToResetWrongAttemptCountInMin));
    }

    @NotNull
    public ResponseEntity<?> getAuthResponse(AuthRequest req, HttpHeaders headers, HttpServletRequest request)
        throws Exception {
            String validEmail = BasicValidator.getValidEmail(req.getEmail());
            if (validEmail == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email provided is not valid"));
            }

            List<User> users = userRepository.findAllByEmail(validEmail);

            AuthRequest.Action action = req.getAction() != null ? req.getAction() : AuthRequest.Action.NONE;

            switch (action) {
                case NONE -> {
                    if (StringUtils.isBlank(req.getPassword())) {
                        return ResponseEntity.badRequest().body(Map.of("message", "Incorrect email or password."));
                    }
                    User authenticatedUser = findPasswordMatchingUser(users, req.getPassword());
                    User primary = users.get(0);
                    if (authenticatedUser == null) {
                        String deviceUuid = getDeviceUuidFromHeaders(headers);
                        LoginLog lastLoginLog = loginLogRepo.getLastLogin(primary.getUserUid());
                        Pair<Boolean, Integer> lockUser = validateLoginLock(headers, primary, deviceUuid, lastLoginLog);
                        throw new InvalidCredentialsException("Incorrect email or password. You have %s retries left".formatted(lockUser.getRight()));
                    }
                }
                default -> {
                    return ResponseEntity.badRequest().body(Map.of("message", "Unsupported action"));
                }
        }
    }

    private String getDeviceUuidFromHeaders( HttpHeaders headers ){
        String deviceUuid = headers.getFirst("X-Internal-User-Uuid");
        if ( deviceUuid == null || StringUtils.isBlank(deviceUuid)) {
            log.error("Device UUID is required to proceed with the OTP verification");
            throw new IllegalArgumentException("Device UUID required to proceed with OTP verification");
        }
        return deviceUuid;
    }

    public void saveLoginLock(String userUid, HttpHeaders headers, LockStatus lockStatus, String deviceUuid) {
        LoginLog loginLog = new LoginLog(userUid, headers, LoginLog.LoginStatus.FAILED, lockStatus, deviceUuid);
        loginLogRepo.saveAndFlush(loginLog);
        log.info("Login lock event: user={}, lockStatus={}", loginLog.getUid(), lockStatus);
    }
}
