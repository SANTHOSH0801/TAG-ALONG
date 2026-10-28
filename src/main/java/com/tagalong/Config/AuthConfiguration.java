package com.tagalong.Config;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthConfiguration {
    private Integer saltByteSize = 36;
    private Integer hashByteSize = 256;
    private Integer jwtExpiryPeriodMins = 15;


    @JsonProperty ("failed_attempt_check_time_in_min")
    @NotNull
    private Integer failedAttemptCheckTimeInMin = 1440;
}
