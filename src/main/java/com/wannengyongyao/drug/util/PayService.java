package com.wannengyongyao.drug.util;

import com.wannengyongyao.drug.util.http.HttpClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Service("payService")
public class PayService {
    @Value("${drug.user.appid}")
    private String appid;

    @Value("${drug.user.pay.mch_id}")
    private String mchId;

    @Value("${drug.user.pay.notify_url}")
    private String notifyUrl;

    @Value("${drug.user.pay.key}")
    private String key;


    /**
     * 获得统一下单参数
     * @param openId
     * @param totalFee
     * @param ip
     * @param body
     * @return
     */
    public String getPayParam(String openId, String outTradeNo, String totalFee, String ip, String body) {
        Map<String, String> datas = new TreeMap<>();
        datas.put("appid", appid);
        datas.put("mch_id", mchId);
        datas.put("device_info", "WEB");
        datas.put("body", body);
        datas.put("trade_type", "JSAPI");
        datas.put("nonce_str", WxUtils.getNonceStr());
        datas.put("notify_url", notifyUrl);
        datas.put("out_trade_no", outTradeNo);
        datas.put("total_fee", totalFee);
        datas.put("openid", openId);
        datas.put("spbill_create_ip", ip);
        String sign = WxUtils.signature(datas, key);
        datas.put("sign", sign);
        return this.getRequestXml(datas);
    }

    /**
     * 得到统一下单参数的xml形式
     *
     * @param parameters
     * @return
     */
    public static String getRequestXml(Map<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(k) ||
                    "body".equalsIgnoreCase(k) ||
                    "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 请求处理
     * @param param
     * @return
     * @throws Exception
     */
    public Map<String, String> requestWechatPayServer(String param){
        String response = HttpClientUtil.doPostHttpsXMLParam("https://api.mch.weixin.qq.com/pay/unifiedorder", param);
        return WxUtils.parseXml(response);
    }

    /**
     * 回调参数解析
     * @param request
     * @return
     * @throws Exception
     */
    public Map<String, String> getCallbackParams(HttpServletRequest request){
        try (InputStream inStream = request.getInputStream();
             ByteArrayOutputStream outSteam = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            String result = new String(outSteam.toByteArray(), "utf-8");
            return WxUtils.parseXml(result);
        } catch (Exception e){
            return null;
        }
    }
}
