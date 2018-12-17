package com.jackeymm.sms.sdk.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

public abstract class EncryptionUtil {

//  初始化keyPair
    public abstract Map initKey () throws Exception ;
//  加密
    public abstract byte[] decrypt(byte[] encryptText,String privateKeyStr)throws Exception;
//  解密
    public abstract byte[] encrypt(byte[] plainText,String publicKeyStr)throws Exception;
//  验签
    public abstract boolean verify(byte[] data,byte[] sign,String publicKeyStr) throws Exception;
//  加签
    public abstract byte[] sign(byte[] data,String privateKeyStr) throws Exception;
//  解码返回byte
    public abstract byte[] decryptBASE64(String key) throws Exception ;
//  编码返回字符串
    public abstract String encryptBASE64(byte[] key) throws Exception ;
//  获取私钥
    public abstract PrivateKey getPrivateKey(String key) throws Exception ;
//  获取公钥
    public abstract PublicKey getPublicKey(String key) throws Exception ;
//  获取String类型的私钥
    public abstract String getPrivateKeyStr(Map<String, Object> keyMap) throws Exception ;
//  获取String类型的公钥
    public abstract String getPublicKeyStr(Map<String, Object> keyMap) throws Exception ;

}
