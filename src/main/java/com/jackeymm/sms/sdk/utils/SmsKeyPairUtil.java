package com.jackeymm.sms.sdk.utils;

import com.jackeymm.sms.sdk.config.BeanFactory;
import com.jackeymm.sms.sdk.domains.KeyPair;
import com.jackeymm.sms.sdk.exceptions.*;
import com.jackeymm.sms.sdk.infrastructure.EhCache;

import java.util.Optional;

public class SmsKeyPairUtil {

    private HttpUtil httpUtil;

    private EhCache ehcache;

    private EncryptionUtil encryptionUtil;

    public SmsKeyPairUtil(){
        httpUtil = BeanFactory.getHttpUtilInstance();
        ehcache = BeanFactory.getEhcacheInstance();
        encryptionUtil = BeanFactory.getEncryptionUtilInstance();
    }

    SmsKeyPairUtil(HttpUtil httpUtil, EhCache ehcache, EncryptionUtil encryptionUtil){
        this.httpUtil = httpUtil;
        this.ehcache = ehcache;
        this.encryptionUtil = encryptionUtil;
    }

    public KeyPair register(String token, String temail) {
        checkToken(token);
        KeyPair keyPair = httpUtil.registerKeypair(token, temail);
        if(null != keyPair){
            ehcache.put(temail, keyPair);
        }
        return keyPair;
    }

    private void checkToken(String token){
        if(StringUtil.isEmpty(token) || !"smsToken".equals(token)){
            throw new RegisterByWrongTokenException();
        }
    }

    public KeyPair queryKeyPairByTemail(String token, String temail) {
        if(StringUtil.isEmpty(token) || StringUtil.isEmpty(temail) ){
            throw new WrongInputException("token : "+token+"|temail : "+temail);
        }
        KeyPair keyPair;
        Optional op = ehcache.get(temail);
        if(op.isPresent()){
            keyPair = (KeyPair)op.get();
        }else {
            keyPair = httpUtil.queryKeyPairByTemail(token, temail);
            if(null != keyPair){
                ehcache.put(temail, keyPair);
            }
        }
        if(null == keyPair || null == keyPair.getPrivateKey() || "".equals(keyPair.getPrivateKey())){
            throw new QueryKeyPairIsNullException("QueryKeyPairIsNull : "+temail);
        }
        return keyPair;
    }


    public String encrypt(String token , String temail, String encryptString) {
        if(StringUtil.isEmpty(token) || StringUtil.isEmpty(temail) || StringUtil.isEmpty(encryptString)){
            throw new WrongInputException("token : "+token+"|temail : "+temail +"| encrypt : "+encryptString);
        }

        KeyPair keyPair = queryKeyPairByTemail(token, temail);

        try {
            return StringUtil.byte2Base64StringFun(encryptionUtil.encrypt(StringUtil.base64String2ByteFun(encryptString), keyPair.getPublicKey()));
        } catch (Exception e) {
            throw new EncryptException(e.getMessage());
        }
    }

    public String decrypt(String token, String temail, String decryptString) {
        if(StringUtil.isEmpty(token) || StringUtil.isEmpty(temail) || StringUtil.isEmpty(decryptString)){
            throw new DecryptByWrongInputException("token : "+token+"| temail :" + temail + "| decrypt : "+ decryptString);
        }

        KeyPair keyPair = queryKeyPairByTemail(token, temail);

        try {
            return StringUtil.byte2Base64StringFun(encryptionUtil.decrypt(StringUtil.base64String2ByteFun(decryptString), keyPair.getPrivateKey()));
        } catch (Exception e) {
            throw new DecryptException(e.getMessage());
        }

    }


    public String sign(String token, String temail, String strData) {
        if(StringUtil.isEmpty(token) || StringUtil.isEmpty(temail) || StringUtil.isEmpty(strData)){
            throw new WrongInputException("token : "+token+"| temail :" + temail + "| sign : "+ strData);
        }

        KeyPair keyPair = queryKeyPairByTemail(token, temail);

        try {
            return StringUtil.byte2Base64StringFun(encryptionUtil.sign(StringUtil.base64String2ByteFun(strData), keyPair.getPrivateKey()));
        } catch (Exception e) {
            throw new RSAException(e.getMessage());
        }
    }

    public boolean verify(String token, String temail, String strData, String strSign) {
        if(StringUtil.isEmpty(token) ||StringUtil.isEmpty(temail) || StringUtil.isEmpty(strData) || StringUtil.isEmpty(strSign)){
            throw new WrongInputException("token : "+token+"|temail :" + temail + "| sign : "+ strSign + "| data : " + strData);
        }

        KeyPair keyPair = queryKeyPairByTemail(token, temail);

        try {
            return encryptionUtil.verify(StringUtil.base64String2ByteFun(strData), StringUtil.base64String2ByteFun(strSign), keyPair.getPublicKey());
        } catch (Exception e) {
            throw new RSAException(e.getMessage());
        }
    }
}
