package com.jackeymm.sms.sdk.exceptions;

public class DecryptByWrongInputException extends RuntimeException {
    public DecryptByWrongInputException(String message){
        super(message);
    }
}
