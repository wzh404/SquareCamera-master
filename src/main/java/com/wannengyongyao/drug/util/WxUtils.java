package com.wannengyongyao.drug.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
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
import java.security.MessageDigest;
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
        Document document = Jsoup.parse(xml);
        Element root = document.select("xml").first();
        for (Element e : root.children()) {
            map.put(e.nodeName(), e.text());
        }
        return map;
    }

    /**
     * 微信支付加密工具
     * @param key
     * @param map
     */
    public static String signature(Map<String, String> map, String key) {
        Set<String> keySet = map.keySet();
        String[] str = new String[map.size()];
        StringBuilder tmp = new StringBuilder();
        // 进行字典排序
        str = keySet.toArray(str);
        Arrays.sort(str);
        for (int i = 0; i < str.length; i++) {
            String t = str[i] + "=" + map.get(str[i]) + "&";
            tmp.append(t);
        }
        if (!StringUtils.isEmpty(key)) {
            tmp.append("key=" + key);
        }
        String tosend = tmp.toString();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(tosend.getBytes("utf-8"));
            String singe = byteToStr(bytes);
            return singe.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 字节数组转换为字符串
     * @param byteArray
     * @return
     */
    public static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 字节转换为字符串
     * @param mByte
     * @return
     */
    public static String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }
}
