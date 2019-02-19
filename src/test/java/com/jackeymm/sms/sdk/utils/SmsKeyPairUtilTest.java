package com.jackeymm.sms.sdk.utils;

import com.jackeymm.sms.sdk.config.BeanFactory;
import com.jackeymm.sms.sdk.domains.KeyPair;
import com.jackeymm.sms.sdk.exceptions.*;
import com.jackeymm.sms.sdk.infrastructure.EhCache;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SmsKeyPairUtilTest {

    private String token = "smsToken";
    private String email = "a@email.com";
    private Map keyMap;
    private String privateKey;
    private String publicKey;
    private KeyPair keyPair;
    private HttpUtil httpUtil = mock(HttpUtil.class);
    private EhCache ehcache = mock(EhCache.class);
    private EncryptionUtil encryptionUtil = BeanFactory.getEncryptionUtilInstance();
    private SmsKeyPairUtil smsKeyPairUtil = new SmsKeyPairUtil(httpUtil, ehcache, encryptionUtil);
    private String strTest = "RSA test";

    @Before
    public void init (){

        try {
            this.keyMap = encryptionUtil.initKey();
            this.privateKey = encryptionUtil.getPrivateKeyStr(keyMap);
            this.publicKey = encryptionUtil.getPublicKeyStr(keyMap);
            this.keyPair = new KeyPair(token, email, privateKey, publicKey);
        } catch (Exception e) {
            throw new RSAException(e.getMessage());
        }
    }

    @Test(expected = RegisterByWrongTokenException.class)
    public void registerFailedByWrongToken(){
        smsKeyPairUtil.register("wrongToken", "a@email.com");
    }

    @Test
    public void registerKeyPairSuccessfully(){
        when(httpUtil.registerKeypair(any(String.class), any(String.class))).thenReturn(keyPair);
        KeyPair keyPair = smsKeyPairUtil.register(token, email);
        assertThat(keyPair).isNotNull();
        assertThat(keyPair.getPrivateKey()).isNotNull();
        assertThat(keyPair.getPublicKey()).isNotNull();
        assertThat(keyPair.getEmail()).isEqualTo(email);
        assertThat(keyPair.getToken()).isEqualTo(token);

    }

    @Test(expected = QueryKeyPairIsNullException.class)
    public void queryKeyPairByEmailNotFound(){
        smsKeyPairUtil.queryKeyPairByEmail(this.token, this.email);
    }

    @Test
    public void  QueryKeyPairByEmailSuccessfully(){
        when(httpUtil.queryKeyPairByEmail(any(String.class), any(String.class))).thenReturn(this.keyPair);
        KeyPair keyPair = smsKeyPairUtil.queryKeyPairByEmail(this.token, this.email);
        assertThat(keyPair).isNotNull();
        assertThat(keyPair.getPublicKey()).isNotNull();
    }

    @Test(expected = WrongInputException.class)
    public void encryptFailedByWrongInput(){
        smsKeyPairUtil.encrypt(null,null, "");
    }


    @Test
    public void encryptSuccessfully(){
        when(ehcache.get(any(String.class))).thenReturn(Optional.ofNullable(this.keyPair));
        String result = smsKeyPairUtil.encrypt(token, email, strTest);
        // System.out.println("result : "+result);
        assertThat(result).isNotNull();
        assertThat(result).isNotEqualTo(strTest);
    }

    @Test(expected = DecryptByWrongInputException.class)
    public void decryptFaileByWrongInput(){
        smsKeyPairUtil.decrypt(null, null, null);
    }

    @Test
    public void decryptSuccessfully(){
        when(ehcache.get(any(String.class))).thenReturn(Optional.ofNullable(this.keyPair));
        String encrypt = smsKeyPairUtil.encrypt(token, email, strTest);
        // System.out.println("encrypt : "+encrypt);
        String result = smsKeyPairUtil.decrypt(token, this.email, encrypt);
        // System.out.println("result : "+result);
        assertThat(result).isNotNull();
        assertThat(result).isNotEqualTo(encrypt);
    }

    @Test(expected = WrongInputException.class)
    public void signWithWrongInputFailed(){
        smsKeyPairUtil.sign(null,null, null);
    }

    @Test
    public void signSuccessfully(){
        when(ehcache.get(any(String.class))).thenReturn(Optional.ofNullable(this.keyPair));
        String strSign = smsKeyPairUtil.sign(token, email, strTest);
        // System.out.println("strSign : " + strSign);
        assertThat(strSign).isNotNull();
        assertThat(strSign).isNotEqualTo(strTest);
    }

    @Test(expected = WrongInputException.class)
    public void verifyWithWrongInputFailed(){
        smsKeyPairUtil.verify(null, null, null, null);
    }

    @Test
    public void verifySuccessfully(){
        when(ehcache.get(any(String.class))).thenReturn(Optional.ofNullable(this.keyPair));
        String strSign = smsKeyPairUtil.sign(token, email, strTest);
        // System.out.println("sign : "+strSign);
        boolean result = smsKeyPairUtil.verify(token, email, strTest, strSign);
        assertThat(result).isEqualTo(true);
    }

}
