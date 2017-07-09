package com.github.wxiaoqi.gate.back.secruity;

import java.io.Serializable;

public class ApiAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    private String clientId;
    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }



    public ApiAuthenticationRequest() {
        super();
    }

    public ApiAuthenticationRequest(String clientId, String secret) {
        this.setClientId(clientId);
        this.setSecret(secret);
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
