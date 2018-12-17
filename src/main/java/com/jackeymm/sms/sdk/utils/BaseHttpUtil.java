package com.jackeymm.sms.sdk.utils;

import com.jackeymm.sms.sdk.domains.KeyPair;

import java.util.Map;

public abstract class BaseHttpUtil {

    public abstract KeyPair registerKeypair(String token, String temail);

    public abstract KeyPair queryKeyPairByTemail(String token, String temail);

    public abstract String postMap(String url, Map<String,String> map);

    public abstract String get(String url);

}
