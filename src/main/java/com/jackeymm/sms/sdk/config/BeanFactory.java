package com.jackeymm.sms.sdk.config;

import com.jackeymm.sms.sdk.infrastructure.Ehcache;
import com.jackeymm.sms.sdk.utils.HttpUtil;

public class BeanFactory {

    private static HttpUtil httpUtil;
    private static Ehcache ehcache;

    public static HttpUtil getHttpUtilInstance(){
        if(null == httpUtil){
            httpUtil = new HttpUtil();
        }
        return httpUtil;
    }

    public static Ehcache getEhcacheInstance() {
        if(null == ehcache){
            ehcache = new Ehcache();
        }
        return ehcache;
    }
}
