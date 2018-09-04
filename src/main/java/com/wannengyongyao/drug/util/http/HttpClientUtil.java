package com.wannengyongyao.drug.util.http;

import com.wannengyongyao.drug.common.config.WxPaySslConfig;
import com.wannengyongyao.drug.common.exception.DrugPayException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created on 2017/5/21.
 *
 * @author stormma
 */
public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String doPostHttpsXMLParam(String url, String param) {
        return doPostHttpsXMLParam(url, param, null);
    }
    /**
     * fashttps xml参数的请求
     * @param url
     * @param param
     * @return
     */
    public static String doPostHttpsXMLParam(String url, String param, WxPaySslConfig sslConfig) {
        String result = null;
        HttpClientBuilder httpClientBuilder = createHttpClientBuilder(sslConfig);

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(createEntry(param));
        try (CloseableHttpClient httpClient = httpClientBuilder.build()){
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, StandardCharsets.UTF_8);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        finally {
            httpPost.releaseConnection();
        }
        return result;
    }

    private static StringEntity createEntry(String requestStr) {
        try {
            return new StringEntity(new String(requestStr.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    private static HttpClientBuilder createHttpClientBuilder(WxPaySslConfig sslConfig) throws DrugPayException {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        if (sslConfig != null) {
            initSSLContext(httpClientBuilder, sslConfig);
        }

        return httpClientBuilder;
    }

    private static void initSSLContext(HttpClientBuilder httpClientBuilder, WxPaySslConfig sslConfig) throws DrugPayException {
        SSLContext sslContext = sslConfig.getSslContext();
        if (null == sslContext) {
            sslContext = sslConfig.initSSLContext();
        }

        SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1"},
                null,
                new DefaultHostnameVerifier());
        httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
    }
}
