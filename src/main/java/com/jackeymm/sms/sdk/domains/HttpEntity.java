package com.jackeymm.sms.sdk.domains;

public class HttpEntity {
    private int statusCode;
    private String returnMsg;

    public HttpEntity(int statusCode, String returnMsg){
        this.statusCode = statusCode;
        this.returnMsg = returnMsg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
}
