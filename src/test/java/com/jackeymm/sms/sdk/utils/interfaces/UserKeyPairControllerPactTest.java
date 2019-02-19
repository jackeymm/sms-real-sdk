package com.jackeymm.sms.sdk.utils.interfaces;

import au.com.dius.pact.consumer.ConsumerPactTestMk2;
import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackeymm.sms.sdk.config.BeanFactory;
import com.jackeymm.sms.sdk.domains.HttpEntity;
import com.jackeymm.sms.sdk.utils.HttpUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.assertj.core.api.Assertions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;

public class UserKeyPairControllerPactTest extends ConsumerPactTestMk2 {

    private ObjectMapper objectMapper = new ObjectMapper();
    private String token = "smsToken";
    private String REGISTER = "/register";

    private String email = "a10@email.com";

    @Override
    protected RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        try {
            return builder
                    .given("Register - a10 register success")
                    .uponReceiving("request 4 register KeyPair")
                    .method("POST")
                    .body("token=smsToken&email=a10@email.com")
                    .path(REGISTER)
                    .headers(headers)
                    .willRespondWith()
                    .status(201)
                    .headers(singletonMap("Content-Type", "application/json"))
                    .body(objectMapper.writeValueAsString(new HttpEntity(201, "success")))
                    .toPact();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected String providerName() {
        return "sms-server";
    }

    @Override
    protected String consumerName() {
        return "sms-sdk";
    }

    @Override
    protected void runTest(MockServer mockServer) throws IOException {
        HttpUtil httpUtil = BeanFactory.getHttpUtilInstance();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("token", token);
        paramMap.put("email", email);
        String registerUrl = mockServer.getUrl() + REGISTER;
        List<NameValuePair> pairs = new ArrayList<>();
        for(Map.Entry<String,String> entry : paramMap.entrySet()){
            pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }
        CloseableHttpResponse response = httpUtil.sendPost(registerUrl, pairs);

        Assertions.assertThat(response.getStatusLine().getStatusCode()).isEqualTo(201);
    }

}
