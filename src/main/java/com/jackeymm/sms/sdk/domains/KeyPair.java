package com.jackeymm.sms.sdk.domains;

import java.io.Serializable;

public class KeyPair implements Serializable {
    private String token;
    private String email;
    private String publicKey;
    private String privateKey;

    public KeyPair(){}

    public KeyPair(String token, String email){
        this.token = token;
        this.email = email;
    }

    public KeyPair(String token, String email, String privateKey, String publicKey) {
        this.token = token;
        this.email = email;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
