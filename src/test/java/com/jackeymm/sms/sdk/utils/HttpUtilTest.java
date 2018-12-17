package com.jackeymm.sms.sdk.utils;

import com.jackeymm.sms.sdk.config.BeanFactory;
import com.jackeymm.sms.sdk.domains.KeyPair;
import com.jackeymm.sms.sdk.exceptions.WrongInputException;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpUtilTest {

    private HttpUtil httpUtil = BeanFactory.getHttpUtilInstance();

    private String token = "kmsToken";

    private String temail = RandomUtils.nextInt(1, 10000) + "@temail.com";

    @Test(expected = WrongInputException.class)
    public void httpRegisterKeyPairfailed(){
        httpUtil.registerKeypair(null,null);
    }

    @Test
    public void httpRegisterKeypairSuccessfully(){
        KeyPair keyPair = httpUtil.registerKeypair(token, temail);
        assertThat(keyPair).isNotNull();
        assertThat(keyPair.getPublicKey()).isNotNull();
        assertThat(keyPair.getPrivateKey()).isNotNull();
    }

    @Test(expected = WrongInputException.class)
    public void httpQueryKeyPairByWrongInput(){
        httpUtil.queryKeyPairByTemail(null,null);
    }

    @Test
    public void httpQueryKeyPairSuccessfully(){
        KeyPair keyPair1 = httpUtil.registerKeypair(token, temail);
        KeyPair keyPair2 = httpUtil.queryKeyPairByTemail(token, temail);
        assertThat(keyPair1).isEqualToComparingFieldByField(keyPair2);
    }
}
