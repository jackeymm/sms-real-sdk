package com.jackeymm.sms.sdk.utils;

import com.google.gson.Gson;
import com.jackeymm.sms.sdk.config.BeanFactory;
import com.jackeymm.sms.sdk.domains.KeyPair;
import com.jackeymm.sms.sdk.exceptions.EncryptException;
import com.jackeymm.sms.sdk.exceptions.SendHttpException;
import com.jackeymm.sms.sdk.exceptions.WrongInputException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil extends BaseHttpUtil {

//    TODO:需要放在配置文件中
    private String httpPath = "http://localhost:8081";

    private String REGISTER = "/register";

    private String QUERYKEYPAIR = "/queryKeyPair";

    private CloseableHttpClient closeableHttpClient;

    public HttpUtil(){
        closeableHttpClient = BeanFactory.getCloseableHttpClientInstance();
    }

    public KeyPair registerKeypair(String token, String email) {
        if(StringUtil.isEmpty(token) || StringUtil.isEmpty(email)){
            throw new WrongInputException("token : " + token + "| email : " + email);
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("token", token);
        paramMap.put("email", email);

        String registerUrl = httpPath + REGISTER;

        return String2KeyPair(postMap(registerUrl, paramMap));
    }

    public KeyPair queryKeyPairByEmail(String token, String email) {
        if(StringUtil.isEmpty(email)){
            throw new WrongInputException("email : " + email);
        }

        String queryUrl = httpPath + QUERYKEYPAIR + "/token/" + token + "/emails/" + email;

        return String2KeyPair(get(queryUrl));
    }

    /**
     * 发送post请求，参数用map接收
     * @param url 地址
     * @param map 参数
     * @return 返回值
     */
    public String postMap(String url, Map<String,String> map) {
        String result = null;
        List<NameValuePair> pairs = new ArrayList<>();
        for(Map.Entry<String,String> entry : map.entrySet()){
            pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }
        try {
            CloseableHttpResponse response = sendPost(url, pairs);
            if(response != null && response.getStatusLine().getStatusCode() == 201){
                HttpEntity entity = response.getEntity();
                result = entityToString(entity);
            }
            return result;
        } catch (IOException e) {
            throw new EncryptException(e.getMessage());
        }finally {
        }
    }

    public CloseableHttpResponse sendPost(String url,  List<NameValuePair> pairs){
        HttpPost post = new HttpPost(url);
        CloseableHttpResponse response;
        try {
            //设置参数到请求对象中
            post.setEntity(new UrlEncodedFormEntity(pairs,"UTF-8"));
            post.setHeader("Content-Type","application/x-www-form-urlencoded");
            response = closeableHttpClient.execute(post);
        } catch (IOException e) {
            throw new SendHttpException(e.getMessage());
        }finally {
        }
        return response;
    }


    private String entityToString(HttpEntity entity) throws IOException {
        String result = null;
        if(entity != null){
            long lenth = entity.getContentLength();
            if(lenth != -1 && lenth < 2048){
                result = EntityUtils.toString(entity,"UTF-8");
            }else {
                InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "UTF-8");
                CharArrayBuffer buffer = new CharArrayBuffer(2048);
                char[] tmp = new char[1024];
                int l;
                while((l = reader1.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }
                result = buffer.toString();
            }
        }
        return result;
    }

    /**
     * get请求，参数拼接在地址上
     * @param url 请求地址加参数
     * @return 响应
     */
    public String get(String url){
        String result = null;
        HttpGet get = new HttpGet(url);
        get.setHeader("Content-Type","application/x-www-form-urlencoded");
        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(get);
            if(response != null && response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                result = entityToString(entity);
            }
            return result;
        } catch (IOException e) {
            throw new EncryptException(e.getMessage());
        }finally {
        }
    }

    public KeyPair String2KeyPair(String httpResult) {
        Gson gson = new Gson();
        Map resultMap = gson.fromJson(httpResult, Map.class);
        Map<String, String> map = (Map<String, String>) resultMap.get("data");

        KeyPair keyPair = new KeyPair();
        keyPair.setToken(map.get("token"));
        keyPair.setPrivateKey(map.get("privateKey"));
        keyPair.setPublicKey(map.get("publicKey"));
        keyPair.setEmail(map.get("email"));
        return keyPair;
    }


}
