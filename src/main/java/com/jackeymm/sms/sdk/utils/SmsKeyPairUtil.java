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
            throw new WrongInputException("temail : "+temail +"| encrypt : "+encryptString);
        }

        KeyPair keyPair = getKeyPairByTemail(temail);

        try {
            return StringUtil.byte2Base64StringFun(RSAKeyPairUtil.encrypt(StringUtil.base64String2ByteFun(encryptString), keyPair.getPublicKey()));
        } catch (Exception e) {
            throw new EncryptException(e.getMessage());
        }
    }

    public String decrypt(String temail, String decryptString) {
        if(StringUtil.isEmpty(temail) || StringUtil.isEmpty(decryptString)){
            throw new DecryptByWrongInputException("temail :" + temail + "| decrypt : "+ decryptString);
        }

        KeyPair keyPair = getKeyPairByTemail(temail);

        try {
            return StringUtil.byte2Base64StringFun(RSAKeyPairUtil.decrypt(StringUtil.base64String2ByteFun(decryptString), keyPair.getPrivateKey()));
        } catch (Exception e) {
            throw new DecryptException(e.getMessage());
        }

    }

    private KeyPair getKeyPairByTemail(String temail){
        KeyPair keyPair = ehcache.get(temail);
        return keyPair;
    }

    public String sign(String temail, String strData) {
        if(StringUtil.isEmpty(temail) || StringUtil.isEmpty(strData)){
            throw new WrongInputException("temail :" + temail + "| sign : "+ strData);
        }

        KeyPair keyPair = getKeyPairByTemail(temail);

        try {
            return StringUtil.byte2Base64StringFun(RSAKeyPairUtil.sign(StringUtil.base64String2ByteFun(strData), keyPair.getPrivateKey()));
        } catch (Exception e) {
            throw new RSAException(e.getMessage());
        }
    }

    public boolean verify(String temail, String strData, String strSign) {
        if(StringUtil.isEmpty(temail) || StringUtil.isEmpty(strData) || StringUtil.isEmpty(strSign)){
            throw new WrongInputException("temail :" + temail + "| sign : "+ strSign + "| data : " + strData);
        }

        KeyPair keyPair = getKeyPairByTemail(temail);

        try {
            return RSAKeyPairUtil.verify(StringUtil.base64String2ByteFun(strData), StringUtil.base64String2ByteFun(strSign), keyPair.getPublicKey());
        } catch (Exception e) {
            throw new RSAException(e.getMessage());
        }
    }
}
