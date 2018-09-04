package com.wannengyongyao.drug.common.config;

import com.wannengyongyao.drug.common.exception.DrugPayException;
import com.wannengyongyao.drug.util.http.HttpClientUtil;
import lombok.Data;
import org.apache.http.ssl.SSLContexts;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;

@Data
public class WxPaySslConfig {
    private String mchId;
    private String keyPath;
    private SSLContext sslContext;

    private InputStream loadKeyStore() throws DrugPayException {
        final String prefix = "classpath:";
        InputStream inputStream;
        if (this.keyPath.equals(prefix)) {
            String path = this.keyPath.substring(prefix.length());
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            inputStream = HttpClientUtil.class.getResourceAsStream(path);
            if (inputStream == null) {
                throw new DrugPayException("Key file not found");
            }
        } else {
            try {
                File file = new File(this.keyPath);
                if (!file.exists()) {
                    throw new DrugPayException("Key file not found");
                }

                inputStream = new FileInputStream(file);
            } catch (IOException e) {
                throw new DrugPayException("Open key file failed.", e);
            }
        }

        return inputStream;
    }

    public SSLContext initSSLContext() throws DrugPayException{
        InputStream inputStream = loadKeyStore();;
        try {

            KeyStore keystore = KeyStore.getInstance("PKCS12");
            char[] partnerId2charArray = this.mchId.toCharArray();
            keystore.load(inputStream, partnerId2charArray);
            SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keystore, partnerId2charArray).build();
            return sslContext;
        } catch (Exception e) {
            throw new DrugPayException("证书文件有问题，请核实！", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
