package com.jackeymm.sms.sdk.utils;

public class StringUtil {
    public static boolean isEmpty(String str) {

        if(null == str || "" == str){
            return true;
        }
        return false;
    }
}
