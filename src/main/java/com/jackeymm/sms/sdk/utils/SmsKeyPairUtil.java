package com.jackeymm.sms.sdk.utils;

import com.jackeymm.sms.sdk.config.BeanFactory;
import com.jackeymm.sms.sdk.domains.KeyPair;
import com.jackeymm.sms.sdk.exceptions.*;
import com.jackeymm.sms.sdk.infrastructure.Ehcache;

public class SmsKeyPairUtil {

    private HttpUtil httpUtil;

    private Ehcache ehcache;

    SmsKeyPairUtil(){
        httpUtil = BeanFactory.getHttpUtilInstance();
        ehcache = BeanFactory.getEhcacheInstance();
    }

    SmsKeyPairUtil(HttpUtil httpUtil, Ehcache ehcache){
        this.httpUtil = httpUtil;
        this.ehcache = ehcache;
    }

    public KeyPair register(String token, String temail) {
        checkToken(token);
        KeyPair keyPair = httpUtil.registerKeypair(token, temail);
        return keyPair;
    }

    private void checkToken(String token){
        if(StringUtil.isEmpty(token) || !"smsToken".equals(token)){
            throw new RegisterByWrongTokenException();
        }
    }

    public KeyPair queryKeyPairByTemail(String temail) {
        KeyPair keyPair = httpUtil.queryKeyPairByTemail(temail);
        if(null == keyPair || null == keyPair.getPrivateKey() || "".equals(keyPair.getPrivateKey())){
            throw new QueryKeyPairIsNullException("QueryKeyPairIsNull : "+temail);
        }
        return keyPair;
    }


    public String encrypt(String temail, String encryptString) {

        if(StringUtil.isEmpty(temail) || StringUtil.isEmpty(encryptString)){
            throw new EncryptByWrongInputException("temail : "+temail +"| encrypt : "+encryptString);
        }

        String result = null;

        KeyPair keyPair = getKeyPairByTemail(temail);
        try {
            result = new String (RSAKeyPairUtil.encrypt(encryptString.getBytes(), keyPair.getPublicKey()));
        } catch (Exception e) {
            throw new EncryptException(e.getMessage());
        }
        return result;
    }

    public String decrypt(String temail, String decryptString) {
        if(StringUtil.isEmpty(temail) || StringUtil.isEmpty(decryptString)){
            throw new DecryptByWrongInputException("temail :" + temail + "| decrypt : "+ decryptString);
        }
        KeyPair keyPair = getKeyPairByTemail(temail);
        String result = null;

        try {
            result = new String (RSAKeyPairUtil.decrypt(decryptString.getBytes(), keyPair.getPrivateKey()));
        } catch (Exception e) {
            throw new DecryptException(e.getMessage());
        }

        return result;
    }

    private KeyPair getKeyPairByTemail(String temail){
        KeyPair keyPair = ehcache.get(temail);
        return keyPair;
    }
}
