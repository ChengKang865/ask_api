package com.autoask.common.util;

import java.math.BigDecimal;
import java.util.*;

/**
 * 常量类
 *
 * @author hyy
 */
public class Constants {


    /**
     * HTTP请求响应200
     */
    public static final int HTTP_STATUS_OK = 200;


    /**
     * 一天的毫秒数
     */
    public static final Long DAY_MILLISECONDS = 1000 * 60 * 60 * 24L;

    /**
     * 时间格式化参数
     */
    public interface DateFormat {
        String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    }

    /**
     * 编码格式
     */
    public interface Charset {

        /**
         * UTF-8编码
         */
        String UTF8 = "UTF-8";

        /**
         * GBK编码
         */
        String GBK = "GBK";

    }

    /**
     * 登陆人身份类型
     */
    public interface LoginType {
        String USER = "user";
        String STAFF = "staff";
        String FACTORY = "factory";
        String SERVICE_PROVIDER = "service_provider";
        String MECHANIC = "mechanic";
        String OUTLETS = "outlets";
        String SYSTEM = "system";
    }

    /**
     * 商户后台权限类型
     */
    public interface PermissionType {
        String EDIT = "edit";
        String CHECK = "check";
        String ROOT = "root";
        String WEIXIN = "weixin";
    }


    /**
     * 用户登录配置常量
     */
    public interface UserPermission {
        String LOGIN = "login";
    }

    /**
     * 错误码
     */
    public interface ErrCode {
        /**
         * 没有登录的错误码
         */
        int NO_LOGIN = 100;
        /**
         * 没有权限
         */
        int NO_PERMISSION = 101;
    }


    /**
     * cookie相关的参数
     */
    public interface CookieParam {
        /**
         * cookie中的session_id
         */
        String SESSION_ID = "at_session_id";

        String STAFF_SESSION_ID = "staff_session_id";

        String FACTORY_SESSION_ID = "factory_session_id";

        String SERVICE_PROVIDER_SESSION_ID = "service_provider_session_id";
        /**
         * 登录默认的cookie的失效时间
         */
        int DEFAULT_COOKIE_EXPIRE_SECOND = 36000; //2小时
        /**
         * 记住用户名，登录cookie的时间
         */
        int REMEMBER_COOKIE_EXPIRE_SECOND = 129600; //36个小时
    }

    /**
     * mybatis 默认查询时间
     */
    public interface ConditionDeleteFlag {
        String DELETE_FALSE = "delete_flag = false";
    }

    /**
     * 商品的审核状态
     */
    public interface GoodsStatus {
        String TO_CHECK = "to_check";
        String CHECKED = "checked";
        String FAILURE = "failure";

        List<String> QUERY_LIST = Arrays.asList(TO_CHECK, CHECKED);
    }

    /**
     * 商品快照审核状态
     */
    public interface GoodsSnapshotStatus {
        String TO_CHECK = "to_check";
        String CHECKED = "checked";
        String FAILURE = "failure";
    }

    /**
     * 支付类型
     */
    public interface PayType {
        String ALI = "ali";
        String WE_CHAT = "wx";
        String UNION_PAY = "union_pay";
        List NO_CASH_TYPES = Arrays.asList(ALI, WE_CHAT, UNION_PAY);
    }

    public interface BaiduMap {
        String KEY = "36O8zh5N6wBKFp1LBt2l1wOAG6wqo22P";
        String INPUT_TYPE = "json";
        String MAP_URL = "http://api.map.baidu.com/geocoder/v2/";
    }


    public interface GoodsType {
        String ALL = "all";
        String ONLINE = "online";
        String OFFLINE = "offline";
        List<String> TYPE_LIST = Arrays.asList(ALL, ONLINE);
    }

    public interface GoodsConfirmType {
        String SUCCESS = "true";
        String FAILURE = "false";
        List<String> CONFIRM_TYPE_LIST = Arrays.asList(SUCCESS, FAILURE);
    }

    public interface Result {
        String SUCCESS = "success";
        String FAILURE = "failure";
    }

    /**
     * 订单状态
     */
    public interface OrderStatus {
        //等待支付
        String TO_PAY = "to_pay";
        //支付超时
        String EXPIRED = "expired";
        //已支付
        String PAYED = "payed";
        //serviceProvider 确认
        String CONFIRM_SP = "confirm_sp";
        //serviceProvider 拒绝
        String REFUSE_SP = "refuse_sp";
        //已确认(已发货)
        String CONFIRMED = "confirmed";
        //已拒绝
        String REFUSED = "refused";
        //已经退款
        String REFUNDED = "refunded";
        //已收货
        String RECEIVED = "received";
        //已校验
        String VALIDATED = "validated";
        //已完成服务 TODO
        String COMPLETE_S = "complete_s";
        //已评论
        String COMMENT = "comment";


        List<String> SP_SELL_STATUS_LIST = Arrays.asList(PAYED, CONFIRM_SP, CONFIRMED, RECEIVED, VALIDATED, COMPLETE_S, COMMENT);

        List<String> FACTORY_SELL_STATUS_LIST = Arrays.asList(PAYED, CONFIRMED, RECEIVED, COMMENT);

        //订单统计销售状态
        List<String> ALL_SELL_STATUS_LIST = Arrays.asList(PAYED, CONFIRM_SP, REFUSE_SP, CONFIRMED, RECEIVED, VALIDATED, COMPLETE_S, COMMENT);

        //订单支付状态列表
        List<String> ALL_STATUS_LIST = Arrays.asList(PAYED, CONFIRM_SP, REFUSE_SP, CONFIRMED, CONFIRMED, REFUSED, REFUNDED, RECEIVED, VALIDATED, COMPLETE_S, COMMENT);
    }


    /**
     * 服务类型
     */
    public interface OrderServeType {
        String ONLINE = "online";
        String OFFLINE = "offline";
    }

    public interface RecordRelatedType {
        String ORDER = "order";
    }

    public interface MerchantType {
        //服务点(修理厂)
        String SERVICE_PROVIDER = "service_provider";

        //代理工厂
        String FACTORY = "factory";

        //分销点
        String OUTLETS = "outlets";

        //修理工
        String MECHANIC = "mechanic";


        String AUTOASK = "autoask";

        List<String> TYPE_LIST = Arrays.asList(SERVICE_PROVIDER, FACTORY, OUTLETS, MECHANIC);
    }

    public interface QRInfoId {
        String OUTLETS_ID = "outletsId";
        String MECHANIC_ID = "mechanicId";
        String SERVICE_PROVIDER_ID = "serviceProviderId";
        String FACTORY_ID = "factoryId";
    }

    /**
     * 首页的 Product 标签
     */
    public interface IndexProductLabel {
        // NEW GEN
        String NEW_GEN = "new_gen";

        // CLASSIC
        String CLASSIC = "classic";

        //明星产品
        String HOT = "hot";

        String CLASSIC_OTHERS = "classic_others";

        List<String> LABEL_LIST = Arrays.asList(NEW_GEN, CLASSIC, HOT, CLASSIC_OTHERS);
    }

    public interface BannerChannel {
        String PC = "pc";

        String M = "h5";

        List<String> CHANNEL_LIST = Arrays.asList(PC, M);
    }

    /**
     * 首页的 Product 渠道
     */
    public interface IndexProductChannel {
        // PC
        String PC = "pc";

        // M
        String M = "h5";

        List<String> CHANNEL_LIST = Arrays.asList(PC, M);
    }


    /**
     * 预约订单失效天
     */
    public static final int EXPIRE_DAY = 2;

    public interface ValidateFlag {
        short VALIDATED = 1;
        short NOT_VALIDATE = 0;
    }

    /**
     * 用户评论等级
     */
    public interface CommentRate {
        /**
         * 最差评价
         */
        Integer ONE = 1;
        Integer TWO = 2;
        Integer TREE = 3;
        Integer FOUR = 4;
        /**
         * 最高评价
         */
        Integer FIVE = 5;
        List<Integer> COMMENT_LIST = Arrays.asList(ONE, TWO, TREE, FOUR, FIVE);
    }

    public interface Channel {
        String PC = "pc";
        String M = "m";
        List<String> TYPE_LIST = Arrays.asList(PC, M);
    }

    /**
     * 收入来源类型
     */
    public interface IncomeType {
        /**
         * 广告费 线下 线上都只会给修理厂
         */
        String AD_FEE = "ad_fee";
        /**
         * 服务费
         */
        String SERVICE_FEE = "service_fee";
        /**
         * 引流费
         */
        String PROMOTE_FEE = "promote_fee";
        /**
         * 修理工
         */
        String HANDLE_FEE = "handle_fee";

        /**
         * 工厂费用
         */
        String FACTORY_FEE = "factory_fee";
    }


    /**
     * 回调服务器地址
     */
    public static final String REDIRECT_SERVER = "http://www.autoask.com/";


//    public static final String ALI_SUCCESS_URL = REDIRECT_SERVER + "autoask/callback/ali/success";

    public static final String ALI_M_SUCCESS_URL = REDIRECT_SERVER + "autoask/callback/ali/m";

    public static final String ALI_PC_SUCCESS_URL = REDIRECT_SERVER + "autoask/callback/ali/pc";


    /**
     * 微信 获取 open_id 地址
     */
    public static final String REDIRECT_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri=http://www.autoask.com/autoask/wechat/redirect/&response_type=code&scope=snsapi_base&state={1}&connect_redirect=1#wechat_redirect";

    public static final String RE_PAY_REDIRECT_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri=http://www.autoask.com/autoask/wechat/redirect/re-pay/&response_type=code&scope=snsapi_base&state={1}&connect_redirect=1#wechat_redirect";


    public static final String MERCHANT_OPEN_ID_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri=http://www.autoask.com/autoask/wechat/redirect/merchant/&response_type=code&scope=snsapi_base&state={1}&connect_redirect=1#wechat_redirect";


    /**
     * 商户申请提款的状态
     */
    public interface MerchantShareApplyStatus {
        String APPLYING = "applying";
        String DOING = "doing"; //处理中
        String SUCCESS = "success"; //处理成功
        String FAILURE = "failure"; //处理失败

        List TYPE_LIST = Arrays.asList(APPLYING, DOING, SUCCESS, FAILURE);
    }

    /**
     * 向商户发送红包状态
     */
    public interface MerchantBonusStatus {
        String DOING = "doing"; //处理中
        String SUCCESS = "success"; //处理成功
        String FAILURE = "failure"; //处理失败
    }

    /**
     * 支付日志类型
     */
    public interface PayLogType {
        String APPLY_REQUEST = "AlipayApplyRequest";//支付宝批量支付申请
        String APPLY = "AlipayApply"; //申请支付宝收款
        String CALLBACK = "AlipayCallback";//支付回调
        String PINGPP_BONUS_APPLY = "PingppBonusApply";//Ping++支付申请记录
        String PINGPP_BONUS_CALLBACK = "PingppBonusCallback";//Ping++支付回调
    }

    /**
     * 密码的最低位数
     */
    public static final int PASSWORD_MIN_LENGTH = 6;


    public interface InvoiceType {
        String VAT = "vat";
        String COMMON = "common";

        List TYPE_LIST = Arrays.asList(VAT, COMMON);
    }

    public interface CardStatus {
        String TO_CHECK = "to_check";
        String CHECKED = "checked";
        String USED = "used";
        List<String> NO_DEL_LIST = Arrays.asList(USED);
    }

    public interface CardTypeStatus {
        String TO_CHECK = "to_check";
        String CHECKED = "checked";
    }

    /**
     * 卡的状态由to_use 到 used的时间 间隔段
     */
    public static final long CARD_TO_USE_GAP_SECONDS = 120000;

    /**
     * 富文本配置类型
     */
    public interface RichTextType {

        /**
         * 活动
         */
        String ACTIVITY = "activity";

        /**
         * 服务
         */
        String SERVICE = "service";

        /**
         * 探索
         */
        String EXPLORE = "explore";

        /**
         * 关于我们
         */
        String ABOUT_US = "about_us";

        String HOW_SELECT = "how_select";

        String HOW_BUY = "how_buy";

        String HOW_PAY = "how_pay";

        /**
         * 支持的类型
         */
        List<String> RichTextSupportTypes = Arrays.asList(ACTIVITY, SERVICE, EXPLORE, ABOUT_US, HOW_SELECT, HOW_BUY, HOW_PAY);
    }

    public static final int WX_OPEN_ID_EXPIRE_SECONDS = 7200;

    public interface ShareStatus {
        String NO_SHARE = "no_share";
        String SHARED = "shared";
        String REFUSED = "refused";
    }

    public interface OrderLogOperatorType {
        //下单
        String INSERT = "insert";

        //支付
        String PAY = "pay";

        String REFUSE_SP = "refuse_sp";

        //确认
        String CONFIRM = "confirm";

        String VALIDATE = "validate";

        String REFUSE = "refuse";

        String REFUND = "refund";

        String RECEIVE = "receive";

        String COMPLETE_S = "complete_s";

        String COMMENT = "comment";
    }

    /**
     * 需要提醒无货的数量
     */
    public static final int WARNING_GOODS_NUM = 50;

    /**
     * 免邮寄费用的额度
     */

    public static final BigDecimal FREE_DELIVERY_AMOUNT = BigDecimal.ZERO;


    public static final String M_INDEX_URL = Constants.REDIRECT_SERVER + "mobile/index.html";

    public static final String INCOME_REFUND_TIME = "23:30";

    public static final BigDecimal TAX_RATE = new BigDecimal(0.80);
}
