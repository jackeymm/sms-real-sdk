package com.jackeymm.sms.sdk.utils;

import com.jackeymm.sms.sdk.domains.KeyPair;
import com.jackeymm.sms.sdk.exceptions.*;
import com.jackeymm.sms.sdk.infrastructure.Ehcache;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SmsKeyPairUtilTest {

    private String token = "smsToken";
    private String temail = "a@temail.com";
    private Map keyMap;
    private String privateKey;
    private String publicKey;
    private KeyPair keyPair;
    private HttpUtil httpUtil = Mockito.mock(HttpUtil.class);
    private Ehcache ehcache = Mockito.mock(Ehcache.class);
    private SmsKeyPairUtil smsKeyPairUtil = new SmsKeyPairUtil(httpUtil, ehcache);
    private String strTest = "RSA test";
    private String encryptString;
    private String decryptString;


    @Before
    public void init (){

        try {
            this.keyMap = RSAKeyPairUtil.initKey();
            this.privateKey = RSAKeyPairUtil.getPrivateKeyStr(keyMap);
            this.publicKey = RSAKeyPairUtil.getPublicKeyStr(keyMap);
            this.keyPair = new KeyPair(token, temail, privateKey, publicKey);
//            this.encryptString = new String(RSAKeyPairUtil.encrypt(strTest.getBytes(), publicKey));
//            this.decryptString = new String(RSAKeyPairUtil.decrypt(encryptString.getBytes(), privateKey));

        } catch (Exception e) {
            throw new RSAException(e.getMessage());
        }
    }

    @Test(expected = RegisterByWrongTokenException.class)
    public void registerFailedByWrongToken(){
        smsKeyPairUtil.register("wrongToken", "a@temail.com");
    }

    @Test
    public void registerKeyPairSuccessfully(){
        when(httpUtil.registerKeypair(any(String.class), any(String.class))).thenReturn(keyPair);
        KeyPair keyPair = smsKeyPairUtil.register(token, temail);
        assertThat(keyPair).isNotNull();
        assertThat(keyPair.getPrivateKey()).isNotNull();
        assertThat(keyPair.getPublicKey()).isNotNull();
        assertThat(keyPair.getTemail()).isEqualTo(temail);
        assertThat(keyPair.getToken()).isEqualTo(token);

    }

//    @Test(expected = RegisterExistExceptin.class)
//    public void registerKeyPairExistFailed(){
//        when(httpUtil.registerKeypair(any(String.class), any(String.class))).thenReturn(keyPair);
//        KeyPair keyPair = smsKeyPairUtil.register(token, temail);
//        assertThat(keyPair).isNotNull();
//        smsKeyPairUtil.register(token, temail);
//
//    }

    @Test(expected = QueryKeyPairIsNullException.class)
    public void queryKeyPairByTemailNotFound(){
        smsKeyPairUtil.queryKeyPairByTemail(this.temail);
    }

    @Test
    public void  QueryKeyPairByTemailSuccessfully(){
        when(httpUtil.queryKeyPairByTemail(any(String.class))).thenReturn(this.keyPair);
        KeyPair keyPair = smsKeyPairUtil.queryKeyPairByTemail(this.temail);
        assertThat(keyPair).isNotNull();
        assertThat(keyPair.getPublicKey()).isNotNull();
    }

    @Test(expected = WrongInputException.class)
    public void encryptFailedByWrongInput(){
        smsKeyPairUtil.encrypt(null, "");
    }


    @Test
    public void encryptSuccessfully(){
        when(ehcache.get(any(String.class))).thenReturn(this.keyPair);
        String result = smsKeyPairUtil.encrypt(temail, strTest);
        System.out.println("result : "+result);
        assertThat(result).isNotNull();
        assertThat(result).isNotEqualTo(encryptString);
    }

    @Test(expected = DecryptByWrongInputException.class)
    public void decryptFaileByWrongInput(){
        smsKeyPairUtil.decrypt(null, null);
    }

    @Test
    public void decryptSuccessfully(){
        when(ehcache.get(any(String.class))).thenReturn(this.keyPair);
        String encrypt = smsKeyPairUtil.encrypt(temail, strTest);
        System.out.println("encrypt : "+encrypt);
        String result = smsKeyPairUtil.decrypt(this.temail, encrypt);
        System.out.println("result : "+result);
        assertThat(result).isNotNull();
        assertThat(result).isNotEqualTo(encrypt);
    }

    @Test(expected = WrongInputException.class)
    public void signWithWrongInputFailed(){
        smsKeyPairUtil.sign(null, null);
    }

    @Test
    public void signSuccessfully(){
        when(ehcache.get(any(String.class))).thenReturn(this.keyPair);
        String strSign = smsKeyPairUtil.sign(temail, strTest);
        System.out.println("strSign : " + strSign);
        assertThat(strSign).isNotNull();
        assertThat(strSign).isNotEqualTo(strTest);
    }

    @Test(expected = WrongInputException.class)
    public void verifyWithWrongInputFailed(){
        smsKeyPairUtil.verify(null, null, null);
    }

    @Test
    public void verifySuccessfully(){
        when(ehcache.get(any(String.class))).thenReturn(this.keyPair);
        String strSign = smsKeyPairUtil.sign(temail, strTest);
        System.out.println("sign : "+strSign);
        boolean result = smsKeyPairUtil.verify(temail, strTest, strSign);
        assertThat(result).isEqualTo(true);
    }

}
