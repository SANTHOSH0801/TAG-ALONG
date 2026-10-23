package com.tagalong.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthResponse {
    private String jwtToken;

    // private Role role;

    private String userUid;

    private String refreshToken;

    private String sessionId;

    private String fullName;

    private Action action;

    private String twoFactorAuthId;

    private String emailVerificationId;

    private String emailTwoFactorAuthId;

    private String encryptedRoleInfo;

    private String accountUid;

    public AuthResponse(
        String jwtToken, 
        String userUid, 
        String refreshToken, 
        String sessionId,
        String fullName,
        String twoFactorAuthId,
        String emailVerificationId,
        String emailTwoFactorAuthId,
        String encryptedRoleInfom,
        String accountUid
) {
        this.jwtToken = jwtToken;
        this.userUid = userUid;
        this.refreshToken = refreshToken;
        this.sessionId = sessionId;
        this.fullName = fullName;
        this.twoFactorAuthId = twoFactorAuthId;
        this.emailVerificationId = emailVerificationId;
        this.emailTwoFactorAuthId = emailTwoFactorAuthId;
        this.encryptedRoleInfo = encryptedRoleInfom;
        this.accountUid = accountUid;
    }

    public enum Action {
        SEND_OTP,
        SEND_OTP_OR_TOTP,
        VERIFY_2AUTH,
        FAILURE,
        SUCCESS,
        REDIRECT_TO_BIZ
    }





}