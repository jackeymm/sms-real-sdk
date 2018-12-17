package com.jackeymm.sms.sdk.config;

import com.jackeymm.sms.sdk.domains.KeyPair;
import com.jackeymm.sms.sdk.exceptions.HttpInstanceException;
import com.jackeymm.sms.sdk.infrastructure.EhCache;
import com.jackeymm.sms.sdk.utils.EncryptionUtil;
import com.jackeymm.sms.sdk.utils.HttpUtil;
import com.jackeymm.sms.sdk.utils.RSAKeyPairUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.time.Duration;

import static org.ehcache.config.builders.ExpiryPolicyBuilder.timeToLiveExpiration;

public class BeanFactory {

    private static CloseableHttpClient closeableHttpClient;
    private static HttpUtil httpUtil;
    private static EhCache ehcache;
//  TODO:通过修改此处进行算法变换
    private static String algorithm = "RSA";
//  TODO:通过修改此处变化http实例
    private static String httpRealized = "httpClient";
    private static RSAKeyPairUtil rsaKeyPairUtil;

    public static synchronized CloseableHttpClient getCloseableHttpClientInstance(){
        if(null == closeableHttpClient){
            closeableHttpClient = HttpClients.createDefault();
        }
        return closeableHttpClient;
    }

    public static synchronized HttpUtil getHttpUtilInstance(){
        switch (httpRealized){
            case "httpClient" :
                if(null == httpUtil){
                    httpUtil = new HttpUtil();
                }
                return httpUtil;
        }
        throw new HttpInstanceException("http instance error");

    }

    public static synchronized EhCache getEhcacheInstance() {
            if (null == ehcache) {
                ehcache = new EhCache(10, String.class, KeyPair.class, timeToLiveExpiration(Duration.ofDays(1)));
            }
            return ehcache;
    }

    public static synchronized EncryptionUtil getEncryptionUtilInstance(){
        switch (algorithm){
            case "RSA" :
                rsaKeyPairUtil = new RSAKeyPairUtil();
                return rsaKeyPairUtil;
//          TODO:添加其他算法工具类
        }
        return null;
    }


}
