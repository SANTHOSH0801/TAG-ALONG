package com.tagalong.service;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tagalong.repository.LoginLogRepository;
import com.tagalong.repository.UserRepository;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

import com.tagalong.dto.AuthRequest;
import com.tagalong.entity.User;
import com.tagalong.exception.InvalidCredentialsException;
import com.tagalong.modal.LoginLog;
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

    public Pair<Boolean, Integer> validateLoginCheck(
        HttpHeaders headers, User user, String deviceUuid, LoginLog lastLoginLog
    ) throws Exception {
        Pair<Boolean, Integer> lockUser = updateLoginAttempt(user, headers, lastLoginLog, deviceUuid);
        
    }

    private final UserRepository userRepository;
    private final LoginLogRepository loginLogRepo;

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
                        Pair<Boolean, Integer> lockUser = validateLoginCheck(headers, primary, deviceUuid, lastLoginLog);
                        throw new InvalidCredentialsException("Incorrect email or password. You have %s retries left".formatted(lockUser.getRight()));
                    }


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
}
