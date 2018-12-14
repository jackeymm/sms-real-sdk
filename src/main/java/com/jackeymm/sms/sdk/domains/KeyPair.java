package com.jackeymm.sms.sdk.domains;

import java.io.Serializable;

public class KeyPair implements Serializable {
    private String token;
    private String temail;
    private String publicKey;
    private String privateKey;

    public KeyPair(){}

    public KeyPair(String token, String temail){
        this.token = token;
        this.temail = temail;
    }

    public KeyPair(String token, String temail, String privateKey, String publicKey) {
        this.token = token;
        this.temail = temail;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTemail() {
        return temail;
    }

    public void setTemail(String temail) {
        this.temail = temail;
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
