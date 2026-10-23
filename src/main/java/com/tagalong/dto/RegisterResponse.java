package com.tagalong.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterResponse {
    
    @JsonProperty("jwt_token")
    private String jwtToken;
    private String email;

    @JsonProperty("user_uid")
    private String userUid;

    @JsonProperty("full_name")
    private String fullName;

    public RegisterResponse(String userUid, String jwtToken, String email, String fullName){
        this.userUid = userUid;
        this.email = email;
        this.fullName = fullName;
        this.jwtToken = jwtToken;
    }
}
