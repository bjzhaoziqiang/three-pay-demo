package com.youth.pay.util;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.youth.pay.dto.AlipayBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhaoziqiang
 * @Description:
 * @date 2020/12/18 15:20
 */
@Slf4j
@Component
public class AliPayUtil {
    @Resource
    private AlipayProperties alipayProperties;


    public String pay(AlipayBean alipayBean) {
        // 1、获得初始化的AlipayClient
        String serverUrl = alipayProperties.getGatewayUrl();
        String appId = alipayProperties.getAppId();
        String privateKey = alipayProperties.getPrivateKey();
        String format = "json";
        String charset = alipayProperties.getCharset();
        String alipayPublicKey = alipayProperties.getPublicKey();
        String signType = alipayProperties.getSignType();
        String returnUrl = alipayProperties.getReturnUrl();
        String notifyUrl = alipayProperties.getNotifyUrl();
        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType);
        //2.设置请求参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        // 页面跳转同步通知页面路径
        request.setReturnUrl(returnUrl);
        // 服务器异步通知页面路径
        request.setNotifyUrl(notifyUrl);
        // 封装参数
        request.setBizContent(JSON.toJSONString(alipayBean));
        log.info("pay param in:{}",JSON.toJSONString(request));
        // 3、请求支付宝进行付款，并获取支付结果
        AlipayTradePagePayResponse response = null;
        String result=null;
        try {
            log.info("********************alipayClient**********************"+alipayClient);
            result = alipayClient.pageExecute(request).getBody();
            log.info("result:{}",result);
        } catch (AlipayApiException e) {
            log.info("");
        }
        return result;
    }
}
