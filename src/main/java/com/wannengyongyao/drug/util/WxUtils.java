package com.wannengyongyao.drug.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.*;

public class WxUtils {
    private static final  Logger logger = LoggerFactory.getLogger(WxUtils.class);
    private static final  String WxUrl = "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code";

    /**
     * 根据小程序临时code，获取登录者的openid
     *
     * @param appid
     * @param secret
     * @param jscode
     *
     * @return openid
     */
    public static Optional<JSONObject> getSessionKey(String appid, String secret, String jscode) {
        StringBuilder url = new StringBuilder(WxUrl);
        url.append("&appid=");
        url.append(appid);
        url.append("&secret=");
        url.append(secret);
        url.append("&js_code=");
        url.append(jscode);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet httpGet = new HttpGet(url.toString());
            HttpResponse response = httpClient.execute(httpGet);
            int httpStatusScode = response.getStatusLine().getStatusCode();
            if (httpStatusScode != 200) {
                logger.error("http resposne status is {}", httpStatusScode);
                return Optional.empty();
            }

            try (BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()))) {
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = bufReader.readLine()) != null) {
                    builder.append(line);
                    builder.append(System.lineSeparator());
                }

                // logger.info(builder.toString());
                JSONObject jsonObject = JSON.parseObject(builder.toString());
                Integer errcode = jsonObject.getInteger("errcode");
                if (errcode == null) {
                    return Optional.of(jsonObject);
                } else {
                    logger.warn("wx errcode is {}", errcode);
                }
            }
        } catch (IOException e) {
            logger.error("get openid exception", e);
        }

        return Optional.empty();
    }

    /**
     *
     * @param encryptedData
     * @param sessionKey
     * @param iv
     * @return
     */
    public static Optional<String> getUserInfo(String sessionKey, String encryptedData, String iv){
        // 被加密的数据
        byte[] dataByte = Base64.decodeBase64(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decodeBase64(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decodeBase64(iv);
        try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");

            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));

            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                return Optional.of(new String(resultByte, "utf-8"));
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * @return
     */
    public static String getNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    /**
     * 解析xml成map
     * @param xml
     * @return
     * @throws Exception
     */
    public static Map<String, String> parseXml(String xml){
        Map<String, String> map = new HashMap<>();
        try {
            Document document = DocumentHelper.parseText(xml);
            List<Element> elements = document.getRootElement().elements();
            for (Element e : elements){
                map.put(e.getName(), e.getText());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 微信支付加密工具
     * @param key
     * @param map
     */
    public static String signature(Map<String, String> map, String key) {
        StringBuilder toSign = new StringBuilder();
        // 进行字典排序
        SortedMap<String, String> sortedMap = new TreeMap<>(map);
        for (String k : sortedMap.keySet()) {
            String value = map.get(k);
            toSign.append(key).append("=").append(value).append("&");
        }
        if (!StringUtils.isEmpty(key)) {
            toSign.append("key=").append(key);
        }
        return DigestUtils.md5Hex(toSign.toString()).toUpperCase();
    }
}
