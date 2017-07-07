package com.autoask.entity.common;

import com.autoask.common.util.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hyy on 2016/12/12.
 */
public class OrderStatusUtil {
    public static List<String> getOnlineStatusList(Integer statusCode) {
        /**
         * <option value="1">待发货</option>
         <option value="2">发货中</option>
         <option value="3">已收货</option>
         <option value="4">已评价</option>
         <option value="5">退款中</option>
         <option value="6">已退款</option>
         7  payed 即 合作工厂待发货 或者是 partner 待发货
         8 pending_partner 即 autoask待发货
         */
        if (null == statusCode) {
            return null;
        }
        switch (statusCode) {
            case 0:
                return null;
            case 1:
                return Arrays.asList(Constants.OrderStatus.TO_PAY);
            case 2:
                return Arrays.asList(Constants.OrderStatus.PAYED);
            case 3:
                return Arrays.asList(Constants.OrderStatus.PAYED, Constants.OrderStatus.CONFIRM_SP);
            case 4:
                return Arrays.asList(Constants.OrderStatus.CONFIRMED);
            case 5:
                return Arrays.asList(Constants.OrderStatus.RECEIVED);
            case 6:
                return Arrays.asList(Constants.OrderStatus.COMPLETE_S, Constants.OrderStatus.COMPLETE_S);
            case 7:
                return Arrays.asList(Constants.OrderStatus.PAYED);
            case 8:
                return null;
            case 9:
                return null;
            case 10:
                return null;
        }
        return null;
    }

    public static List<String> getUserStatusList(Integer statusCode) {
        if (null == statusCode) {
            return null;
        }
//         <option value="0">全部订单</option>
//         <option value="1">待支付</option>
//         <option value="2">待确认</option>
//         <option value="3">待发货</option>
//         <option value="4">待收货</option>
//         <option value="5">待服务</option>
//         <option value="6">待评价</option>
//         <option value="7">已评价</option>
//         <option value="8">支付超</option
//         <option value="9">已退款</option>
//         <option value="10">待确认服务</option>


        switch (statusCode) {
            case 0:
                return null;
            case 1:
                return null;
            case 2:
                return Arrays.asList(Constants.OrderServeType.OFFLINE);
            case 3:
                return null;
            case 4:
                return null;
            case 5:
                return Arrays.asList(Constants.OrderServeType.OFFLINE);
            case 6:
                return null;
            case 7:
                return null;
            case 8:
                return null;
            case 9:
                return null;
            case 10:
                return Arrays.asList(Constants.OrderServeType.OFFLINE);
        }
        return null;
    }

    public static List<String> getUserServeTypeList(Integer statusCode) {
        if (null == statusCode) {
            return null;
        }
        //TODO
        //0 所有的订单 1 待支付订单 2 待确认
        switch (statusCode) {
            case 0:
                return null;
            case 1:
                return Arrays.asList(Constants.OrderStatus.TO_PAY);
            case 2:
                return Arrays.asList(Constants.OrderStatus.PAYED);
            case 3:
                return Arrays.asList(Constants.OrderStatus.COMMENT);
            case 4:
                return null;
            case 5:
                return null;
            case 6:
                return null;
            case 7:
                return null;
            case 8:
                return null;
        }
        return null;
    }
}
