package com.tagalong.Config;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tagalong.Constants.Role;

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

    @JsonProperty("max_allowed_wrong_attempt")
    @NotNull
    private Integer maxAllowedWrongAttempt = 3;

    @JsonProperty ("time_to_reset_wrong_attempt_count_in_min_admin")
    @NotNull
    private Integer timeToResetWrongAttemptCountInMinAdmin=1440;

    @JsonProperty ("time_to_reset_wrong_attempt_count_in_min_user")
    @NotNull
    private Integer timeToResetWrongAttemptCountInMinUser=240;

    public Integer getTimeToResetWrongAttemptCountInMin(Role userRole) {
        if (userRole == Role.ADMIN) {
            return timeToResetWrongAttemptCountInMinAdmin;
        }
        return timeToResetWrongAttemptCountInMinUser;
    }
}
