package com.autoask.pay.pingpp;

import com.autoask.common.exception.ApiException;
import com.autoask.pay.pingpp.config.PingppConfig;
import com.pingplusplus.Pingpp;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeRefundCollection;
import com.pingplusplus.model.Refund;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weid on 16-10-11.
 * <p>
 * Ping++ 退款 util.
 */
public class PingppRefundsUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PingppRefundsUtil.class);

    static {
        Pingpp.apiKey = PingppConfig.liveSecretKey;
        Pingpp.privateKeyPath = PingppConfig.getPrivateKeyFilePath();
    }

    /**
     * 退款
     * <p>
     * 创建退款，需要先获得 charge ,然后调用 charge.getRefunds().create();
     * 参数具体说明参考：https://pingxx.com/document/api#api-r-new
     * <p>
     * 可以一次退款，也可以分批退款。
     *
     * @param chargeId    支付成功后返回的 chargeId. 我方订单在支付完成后需要保存 Ping++ 端的 chargeId.
     * @param amount      退款的金额, 单位为对应币种的最小货币单位，例如：人民币为分（如退款金额为 1 元，此处请填 100）。
     *                    必须小于等于可退款金额，默认为全额退款
     * @param description 退款的描述信息.
     * @return
     */
    public static Refund refund(String chargeId, Integer amount, String description) throws ApiException {
        Charge charge = PingppChargeUtil.retrieve(chargeId);
        if (charge == null) {
            return null;
        }
        Refund refund = null;
        Map<String, Object> params = new HashMap<>();
        params.put("description", description);
        params.put("amount", amount);
        try {
            refund = charge.getRefunds().create(params);
            return refund;
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }

    }

    /**
     * 查询支付成功后的订单是否有退款
     * <p>
     * 根据 Id 查询退款记录。需要传递 charge。
     * 参考文档：https://pingxx.com/document/api#api-r-inquiry
     *
     * @return
     */
    public static Refund retrieve(String chargeId) throws Exception {
        Charge charge = PingppChargeUtil.retrieve(chargeId);
        if (charge == null) {
            return null;
        }
        return charge.getRefunds().retrieve(chargeId);
    }

    /**
     * 根据 chargeId, refundId来查询单笔退款记录
     *
     * @param chargeId 支付成功后返回的 chargeId
     * @param refundId 退款成功后返回的 refundId
     * @return
     * @throws Exception
     */
    public static Refund retrieve(String chargeId, String refundId) throws Exception {
        Charge charge = Charge.retrieve(chargeId);
        return charge.getRefunds().retrieve(refundId);
    }

    /**
     * 分页查询退款
     * <p>
     * 批量查询退款。默认一次 10 条，用户可以通过 limit 自定义查询数目，但是最多不超过 100 条。
     * 参考文档：https://pingxx.com/document/api#api-r-list
     */
    public static ChargeRefundCollection all(String chargeId, Integer limit) throws ApiException {
        Charge charge = PingppChargeUtil.retrieve(chargeId);
        if (charge == null) {
            return null;
        }
        Map<String, Object> refundParams = new HashMap<>();
        refundParams.put("limit", limit);
        try {
            return charge.getRefunds().all(refundParams);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    /**
     * 测试案例
     *
     * @param args
     */
    public static void main(String[] args) {
        String chargeId = "ch_D40SCOPGKmbPv5G0i9qb54iL";
        String description = "autoask测试退款!";
        Refund refund = null;
        try {
            refund = refund(chargeId, 1, description);
            assert refund != null;
            System.out.println(refund.toString());
        } catch (ApiException e) {
            System.out.println(e.getMessage());
        }
    }
}
