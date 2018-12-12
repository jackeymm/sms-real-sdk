package com.jackeymm.sms.sdk.exceptions;

public class RegisterByWrongTokenException extends RuntimeException {
    public RegisterByWrongTokenException(){
        super("register Keypair by wrong token");
    }
}
