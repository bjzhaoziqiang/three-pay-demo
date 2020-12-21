package com.youth.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.youth.pay.dao.UserOrderMapper;
import com.youth.pay.dto.AlipayBean;
import com.youth.pay.entity.UserOrder;
import com.youth.pay.entity.UserOrderExample;
import com.youth.pay.enums.OrderEnum;
import com.youth.pay.service.UserOrderService;
import com.youth.pay.util.AliPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * @author zhaoziqiang
 * @Description:
 * @date 2020/12/18 15:47
 */
@Slf4j
@Service
public class UserOrderServiceImpl implements UserOrderService {
    @Resource
    private UserOrderMapper userOrderMapper;
    @Autowired
    private AliPayUtil aliPayUtil;

    @Transactional
    @Override
    public String orderPay(BigDecimal orderAmount) throws AlipayApiException {

        //1. 产生订单
        UserOrder order = new UserOrder();
        order.setOrderNo(System.currentTimeMillis() + "");
        order.setUserId(UUID.randomUUID().toString());
        order.setOrderAmount(orderAmount);
        order.setOrderStatus(OrderEnum.ORDER_STATUS_NOT_PAY.getStatus());
        String format = "yyyy-MM-dd HH:mm:ss";
        order.setCreateTime(DateFormatUtils.format(new Date(), format));
        order.setLastUpdateTime(DateFormatUtils.format(new Date(), format));

        userOrderMapper.insert(order);

        //2. 调用支付宝
        AlipayBean alipayBean = new AlipayBean();
        alipayBean.setOut_trade_no(order.getOrderNo());
        alipayBean.setSubject("充值:" + order.getOrderAmount());
        alipayBean.setTotal_amount(orderAmount.toString());
        String pay = aliPayUtil.pay(alipayBean);
        System.out.println("pay:" + pay);
        return pay;
    }

    @Override
    public UserOrder getOrderByOrderNo(String orderNo) {
        UserOrderExample userOrderExample = new UserOrderExample();
        UserOrderExample.Criteria criteria = userOrderExample.createCriteria();
        criteria.andOrderNoEqualTo(orderNo);
        return userOrderMapper.selectByExample(userOrderExample).get(0);
    }

    @Override
    public int updateOrder(UserOrder userOrder) {
        return userOrderMapper.updateByPrimaryKey(userOrder);
    }
}
