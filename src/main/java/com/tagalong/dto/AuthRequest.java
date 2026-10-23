package com.tagalong.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude
@JsonNaming (PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthRequest {
    
    private String email;
    private String password;
    private String sessionId;
    private String twoFactorAuthId;
    private String otp;
    private String userAgentType;
    private Action action = Action.NONE;

    public enum Action {
        NONE,
        SEND_OTP,
        VERIFY_2AUTH,
        VERIFY_TOTP
    }
}
