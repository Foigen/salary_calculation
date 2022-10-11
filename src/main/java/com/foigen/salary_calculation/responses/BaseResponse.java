package com.foigen.salary_calculation.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponse {
    private final String status;
    private final Integer cash;
    private final Integer code;
    private final String message;

    @Override
    public String toString() {
        return "{\"status\":\"" + status + "\",\"cash\":" + cash + ",\"code\":" + code + ",\"message\":\"" + message + "\"}";
    }
}
