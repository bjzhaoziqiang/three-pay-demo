package com.youth.pay.service;

import com.alipay.api.AlipayApiException;
import com.youth.pay.entity.UserOrder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author zhaoziqiang
 * @Description:
 * @date 2020/12/18 15:47
 */

public interface UserOrderService {


    /**
     * 下单
     *
     * @param orderNo
     * @return 返回订单信息
     */
    public String orderPay(BigDecimal decimal) throws AlipayApiException;


    /**
     * 根据订单号查询订单
     *
     * @param orderNo
     * @return 返回订单信息
     */
    public UserOrder getOrderByOrderNo(String orderNo);

    /**
     * 更新订单
     *
     * @param userOrder 订单对象
     * @return 返回更新结果
     */
    public int updateOrder(UserOrder userOrder);

}
