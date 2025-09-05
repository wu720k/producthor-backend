package com.producthor.Producthor.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String username;
    @JsonProperty("isAdmin")
    private boolean isAdmin;
    private BaseResponse baseResponse;
}
