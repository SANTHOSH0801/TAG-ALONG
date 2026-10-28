package com.tagalong.modal;

import jakarta.persistence.ManyToOne;

import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tagalong.util.CommonUtil;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;

public class LoginLog extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uid", insertable = false, updatable = false)
    private User user;

    @JsonIgnore
    @JsonProperty("user_id")
    @Column(name = "user_id")
    private String UserId;

    @Column(name = "client_headers", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, List<String>> clientHeaders = new HashMap<>();

    @JsonProperty("login_status")
    @Enumerated(EnumType.STRING)
    private LoginStatus loginStatus;

    @JsonProperty("lock_status")
    @Enumerated(EnumType.STRING)
    private LockStatus lockStatus;

    // Uid of the user who override the lock to allow access to the user
    @JsonProperty("unlocked_by")
    private String unlockedBy;

    // Uid of the admin who manually locked this user (null = auto-lock due to
    // failed attempts)
    @JsonProperty("locked_by")
    private String lockedBy;

    // device id sent by frontend for login
    @JsonProperty("device_id")
    private String deviceId;

    public LoginLog(boolean shouldInitialize) {
        if (shouldInitialize) {
            setUid(CommonUtil.getAlphaNumericUUID());
            this.lockStatus = LockStatus.OPEN;
        }
    }

    public LoginLog(
        String userId,
        HttpHeaders headers,
        LoginStatus loginStatus,
        LockStatus lockStatus,
        String deviceId) {
            this(true);
            this.UserId = userId;
            Map<String, List<String>> headerMap = new HashMap<>();
            if (headers != null) {
                headers.forEach((k, v) -> headerMap.put(k, new ArrayList<>(v)));
            }
            this.clientHeaders = headerMap;
            this.loginStatus = loginStatus;
            this.lockStatus = lockStatus;
            this.deviceId = deviceId;
        }

    public enum LoginStatus {
        PASSWORD_VERIFIED,
        OTP_VERIFICATION_INITIATED,
        INCORRECT_OTP,
        SUCCESS, // Login attempt was successful
        FAILED, // Incorrect password
        LOCKED, // User's account was locked while making this attempt
    }

    public enum LockStatus {
        NONE,
        OPEN, // No lock on user
        LOCKED, // User's account is locked
        RESET // Lock reset by admin
    }

}