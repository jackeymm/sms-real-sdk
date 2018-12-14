package com.jackeymm.sms.sdk.config;

import com.jackeymm.sms.sdk.domains.KeyPair;
import com.jackeymm.sms.sdk.infrastructure.EhCache;
import com.jackeymm.sms.sdk.utils.HttpUtil;

import java.time.Duration;

import static org.ehcache.config.builders.ExpiryPolicyBuilder.timeToLiveExpiration;

public class BeanFactory {

    private static HttpUtil httpUtil;
    private static EhCache ehcache;

    public static HttpUtil getHttpUtilInstance(){
        if(null == httpUtil){
            httpUtil = new HttpUtil();
        }
        return httpUtil;
    }

    public static EhCache getEhcacheInstance() {
        if(null == ehcache){
            ehcache = new EhCache(10, String.class, KeyPair.class,timeToLiveExpiration(Duration.ofDays(1)));
        }
        return ehcache;
    }
}
