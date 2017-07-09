package com.github.wxiaoqi.gate.back.secruity;

import java.io.Serializable;

public class ApiAuthenticationResponse implements Serializable {
    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;

    public ApiAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
