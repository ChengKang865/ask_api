package com.autoask.service.impl.merchant;

import com.autoask.cache.CacheService;
import com.autoask.cache.PhoneService;
import com.autoask.cache.SessionService;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.*;
import com.autoask.dao.StaffDao;
import com.autoask.dao.merchant.*;
import com.autoask.dao.qr.QRCodeDao;
import com.autoask.entity.mongo.Staff;
import com.autoask.entity.mongo.invoice.VatInvoice;
import com.autoask.entity.mongo.merchant.*;
import com.autoask.entity.mongo.qr.QRCode;
import com.autoask.entity.mysql.*;
import com.autoask.mapper.MerchantAssetsMapper;
import com.autoask.mapper.MerchantShareApplyMapper;
import com.autoask.service.merchant.MechanicService;
import com.autoask.service.merchant.MerchantService;
import com.autoask.service.merchant.OutletsService;
import com.autoask.service.merchant.ServiceProviderService;
import com.autoask.service.qr.QRCodeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by weid on 16-11-5.
 */
@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantShareApplyMapper merchantShareApplyMapper;

    @Autowired
    private MerchantAssetsMapper merchantAssetsMapper;

    @Autowired
    private FactoryDao factoryDao;

    @Autowired
    private MechanicDao mechanicDao;

    @Autowired
    private OutletsDao outletsDao;

    @Autowired
    private ServiceProviderDao serviceProviderDao;

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private MechanicService mechanicService;

    @Autowired
    private OutletsService outletsService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private QRCodeService qrCodeService;

    @Override
    public void saveShareApply(String merchantId, String merchantType, BigDecimal amount, BigDecimal fee) throws ApiException {
        BaseMerchant merchantInfo = checkMerchantInfo(merchantId, merchantType);

        MerchantAssets merchantAssets = checkMerchantAssets(merchantId, merchantType, amount, fee);

        //减去申请的额度
        merchantAssetsMapper.updateBalance(merchantAssets.getMerchantType(), merchantAssets.getMerchantId(), amount.multiply(new BigDecimal(-1)));
        //提交提款申请记录
        BigDecimal realAmount = amount.subtract(fee);
        MerchantShareApply shareApply = new MerchantShareApply();
        shareApply.setApplyId(CodeGenerator.uuid());
        shareApply.setMerchantType(merchantType);
        shareApply.setMerchantId(merchantId);
        shareApply.setAccount(merchantInfo.getAliPayAccount());
        shareApply.setAccountName(merchantInfo.getAliPayUserName());
        shareApply.setStatus(Constants.MerchantShareApplyStatus.APPLYING);
        shareApply.setAmount(realAmount);
        shareApply.setFee(fee);
        shareApply.setCreateTime(new Date());
        merchantShareApplyMapper.insert(shareApply);
    }

    private BaseMerchant checkMerchantInfo(String merchantId, String merchantType) throws ApiException {
        BaseMerchant merchantInfo = getMerchantInfo(merchantType, merchantId);
        if (merchantInfo == null) {
            throw new ApiException("商户信息不存在");
        }

        if (StringUtils.isEmpty(merchantInfo.getAliPayAccount()) || StringUtils.isEmpty(merchantInfo.getAliPayUserName())) {
            throw new ApiException("商户支付宝信息为空");
        }
        return merchantInfo;
    }

    private MerchantAssets checkMerchantAssets(String merchantId, String merchantType, BigDecimal amount, BigDecimal fee) throws ApiException {
        MerchantAssets merchantAssets = merchantAssetsMapper.selectForLock(merchantType, merchantId);
        if (merchantAssets == null) {
            throw new ApiException("用户资产信息不存在");
        }
        if (null == amount) {
            throw new ApiException("申请金额不能为空");
        }
        if (amount.compareTo(BigDecimal.valueOf(PropertiesUtil.getProperty("alipay.fee.min", 1))) <= 0) {
            throw new ApiException("申请金额小于手续费");
        }
        if (amount.compareTo(merchantAssets.getBalance()) > 0) {
            throw new ApiException("申请金额大于账户余额");
        }
        //校验手续费
        if (null == fee) {
            throw new ApiException("参数非法");
        }
        BigDecimal calFee = BigDecimalUtil.computeAlipayFee(amount);
        if (calFee.compareTo(fee) != 0) {
            throw new ApiException("手续费参数非法");
        }
        return merchantAssets;
    }

    @Override
    public BaseMerchant getMerchantInfo(String merchantType, String merchantId) throws ApiException {
        Query query = new Query(new Criteria("id").is(merchantId).and("deleteFlag").is(false));
        if (StringUtils.equals(merchantType, Constants.MerchantType.FACTORY)) {
            return factoryDao.findOne(query);
        }
        if (StringUtils.equals(merchantType, Constants.MerchantType.MECHANIC)) {
            return mechanicDao.findOne(query);
        }
        if (StringUtils.equals(merchantType, Constants.MerchantType.OUTLETS)) {
            return outletsDao.findOne(query);
        }
        if (StringUtils.equals(merchantType, Constants.MerchantType.SERVICE_PROVIDER)) {
            return serviceProviderDao.findOne(query);
        }
        if (StringUtils.equals(merchantType, Constants.MerchantType.AUTOASK) && StringUtils.isEmpty(merchantId)) {
            return null;
        }
        throw new ApiException("商户不存在");
    }

    /**
     * 检查商户是否被删除，如果是商户已经被删就抛出异常信息
     *
     * @param merchant
     * @throws ApiException
     */
    private void checkMerchantDeleteFlag(BaseMerchant merchant) throws ApiException {
        if (merchant != null && merchant.getDeleteFlag()) {
            //如果商户存在，且商户已经删除，需要抛出异常信息
            throw new ApiException("商户已经被删除");
        }
    }

    @Override
    public List<String> searchMerchantIdByName(String merchantType, String merchantName) throws ApiException {
        if (StringUtils.isEmpty(merchantName)) {
            return null;
        }
        String loginType = LoginUtil.getSessionInfo().getLoginType();
        String nameReg = ".*" + merchantName + ".*";
        Criteria criteria = new Criteria();
        criteria.and("name").regex(nameReg).and("deleteFlag").is(false);
        if (StringUtils.equals(merchantType, Constants.MerchantType.FACTORY)) {
            List<Factory> factoryList = factoryDao.find(Query.query(criteria));
            if (CollectionUtils.isNotEmpty(factoryList)) {
                ArrayList<String> idList = new ArrayList<>(factoryList.size());
                for (Factory factory : factoryList) {
                    idList.add(factory.getId());
                }
                return idList;
            }
        }  else if (StringUtils.equals(merchantType, Constants.MerchantType.SERVICE_PROVIDER)) {
            List<ServiceProvider> serviceProviderList = serviceProviderDao.find(Query.query(criteria));
            if (CollectionUtils.isNotEmpty(serviceProviderList)) {
                ArrayList<String> idList = new ArrayList<>(serviceProviderList.size());
                for (ServiceProvider serviceProvider : serviceProviderList) {
                    idList.add(serviceProvider.getId());
                }
                return idList;
            }
        } else if (StringUtils.equals(merchantType, Constants.MerchantType.OUTLETS)) {
            List<Outlets> outletsList = outletsDao.find(Query.query(criteria));
            if (CollectionUtils.isNotEmpty(outletsList)) {
                ArrayList<String> idList = new ArrayList<>(outletsList.size());
                for (Outlets outlets : outletsList) {
                    idList.add(outlets.getId());
                }
                return idList;
            }
        } else if (StringUtils.equals(merchantType, Constants.MerchantType.MECHANIC)) {
            List<Mechanic> mechanicList = mechanicDao.find(Query.query(criteria));
            if (CollectionUtils.isNotEmpty(mechanicList)) {
                ArrayList<String> idList = new ArrayList<>(mechanicList.size());
                for (Mechanic mechanic : mechanicList) {
                    idList.add(mechanic.getId());
                }
                return idList;
            }
        }
        return null;
    }

    @Override
    public Map<String, String> getMerchantNameMap(List<BaseMerchant> baseMerchantList) throws ApiException {
        HashSet<String> staffIdSet = new HashSet<>();
        HashSet<String> factoryIdSet = new HashSet<>();
        HashSet<String> partnerIdSet = new HashSet<>();
        HashSet<String> serviceProviderIdSet = new HashSet<>();
        HashSet<String> mechanicIdSet = new HashSet<>();
        HashSet<String> outletsIdSet = new HashSet<>();
        for (BaseMerchant baseMerchant : baseMerchantList) {
            String loginType = baseMerchant.getLoginType();
            String id = baseMerchant.getId();
            if (StringUtils.isNotEmpty(id)) {
                switch (loginType) {
                    case Constants.LoginType.STAFF:
                        staffIdSet.add(id);
                        break;
                    case Constants.LoginType.FACTORY:
                        factoryIdSet.add(id);
                        break;
                    case Constants.LoginType.SERVICE_PROVIDER:
                        serviceProviderIdSet.add(id);
                        break;
                    case Constants.LoginType.MECHANIC:
                        mechanicIdSet.add(id);
                        break;
                    case Constants.LoginType.OUTLETS:
                        outletsIdSet.add(id);
                        break;
                }
            }
        }
        HashMap<String, String> typeId2NameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(staffIdSet)) {
            List<Staff> staffList = staffDao.find(Query.query(Criteria.where("deleteFlag").is(false).and("id").in(staffIdSet)));
            for (Staff staff : staffList) {
                typeId2NameMap.put(Constants.LoginType.STAFF + staff.getId(), staff.getName());
            }
        }
        if (CollectionUtils.isNotEmpty(factoryIdSet)) {
            List<Factory> factoryList = factoryDao.find(Query.query(Criteria.where("deleteFlag").is(false).and("id").in(factoryIdSet)));
            for (Factory factory : factoryList) {
                typeId2NameMap.put(Constants.LoginType.FACTORY + factory.getId(), factory.getName());
            }
        }
        if (CollectionUtils.isNotEmpty(serviceProviderIdSet)) {
            List<ServiceProvider> serviceProviderList = serviceProviderDao.find(Query.query(Criteria.where("deleteFlag").is(false).and("id").in(serviceProviderIdSet)));
            for (ServiceProvider serviceProvider : serviceProviderList) {
                typeId2NameMap.put(Constants.LoginType.SERVICE_PROVIDER + serviceProvider.getId(), serviceProvider.getName());
            }
        }
        if (CollectionUtils.isNotEmpty(mechanicIdSet)) {
            List<Mechanic> factoryList = mechanicDao.find(Query.query(Criteria.where("deleteFlag").is(false).and("id").in(mechanicIdSet)));
            for (Mechanic mechanic : factoryList) {
                typeId2NameMap.put(Constants.LoginType.MECHANIC + mechanic.getId(), mechanic.getName());
            }
        }
        if (CollectionUtils.isNotEmpty(outletsIdSet)) {
            List<Outlets> factoryList = outletsDao.find(Query.query(Criteria.where("deleteFlag").is(false).and("id").in(outletsIdSet)));
            for (Outlets outlets : factoryList) {
                typeId2NameMap.put(Constants.LoginType.OUTLETS + outlets.getId(), outlets.getName());
            }
        }
        return typeId2NameMap;
    }

    @Override
    public void initOrderMerchantName(List<OrderInfo> orderInfoList) throws ApiException {
        if (CollectionUtils.isNotEmpty(orderInfoList)) {
            List<BaseMerchant> baseMerchantList = new ArrayList<>(orderInfoList.size() * 3);
            for (OrderInfo orderInfo : orderInfoList) {
                if (null != orderInfo.getOrderServe()) {
                    OrderServe orderServe = orderInfo.getOrderServe();
                    BaseMerchant mechanicBean = new BaseMerchant();
                    mechanicBean.setLoginType(Constants.LoginType.MECHANIC);
                    mechanicBean.setId(orderServe.getMechanicId());
                    baseMerchantList.add(mechanicBean);
                    BaseMerchant serviceProviderBean = new BaseMerchant();
                    serviceProviderBean.setLoginType(Constants.LoginType.SERVICE_PROVIDER);
                    serviceProviderBean.setId(orderServe.getServiceProviderId());
                    baseMerchantList.add(serviceProviderBean);
                } else if (null != orderInfo.getOrderDelivery()) {
                    OrderDelivery orderDelivery = orderInfo.getOrderDelivery();
                    String merchantType = orderDelivery.getMerchantType();
                    String merchantId = orderDelivery.getMerchantId();
                    if (StringUtils.isNotEmpty(merchantId) && !StringUtils.equals(merchantType, Constants.MerchantType.AUTOASK)) {
                        BaseMerchant baseMerchant = new BaseMerchant();
                        baseMerchant.setLoginType(merchantType);
                        baseMerchant.setId(merchantId);
                        baseMerchantList.add(baseMerchant);
                    }
                }

            }
            Map<String, String> merchantNameMap = this.getMerchantNameMap(baseMerchantList);
            for (OrderInfo orderInfo : orderInfoList) {
                if (null != orderInfo.getOrderServe()) {
                    OrderServe orderServe = orderInfo.getOrderServe();
                    if (StringUtils.isNotEmpty(orderServe.getMechanicId())) {
                        orderServe.setMechanicName(merchantNameMap.get(Constants.LoginType.MECHANIC + orderServe.getMechanicId()));
                    }
                    orderServe.setServiceProviderName(merchantNameMap.get(Constants.LoginType.SERVICE_PROVIDER + orderServe.getServiceProviderId()));
                } else if (null != orderInfo.getOrderDelivery()) {
                    OrderDelivery orderDelivery = orderInfo.getOrderDelivery();
                    String merchantType = orderDelivery.getMerchantType();
                    String merchantId = orderDelivery.getMerchantId();
                    if (StringUtils.isNotEmpty(merchantId) && !StringUtils.equals(merchantType, Constants.MerchantType.AUTOASK)) {
                        orderDelivery.setMerchantName(merchantNameMap.get(merchantType + merchantId));
                    }
                }

            }
        }
    }

    @Override
    public void activeMerchant(String phone, String code, String activeSessionId) throws ApiException {
        String verifyCode = phoneService.getVerifyCode(phone);
        if (!StringUtils.equals(verifyCode, code)) {
            throw new ApiException("验证码错误");
        }
        String openId = sessionService.getActiveSessionId(activeSessionId);
        if (StringUtils.isEmpty(openId)) {
            throw new ApiException("数据异常，请重新操作");
        }
        Query query = Query.query(Criteria.where("phone").is(phone).and("deleteFlag").is(false));
        Mechanic mechanic = mechanicDao.findOne(query);
        Outlets outlets = outletsDao.findOne(query);
        if (null == mechanic && null == outlets) {
            throw new ApiException("手机号码不存在");
        }
        if (null != mechanic) {
            Mechanic updateMechanic = new Mechanic();
            updateMechanic.setActivated(true);
            updateMechanic.setOpenId(openId);

            mechanicDao.updateSelective(mechanic.getId(), updateMechanic);
        }
        if (null != outlets) {
            Outlets updateOutlets = new Outlets();
            updateOutlets.setActivated(true);
            updateOutlets.setOpenId(openId);

            outletsDao.updateSelective(outlets.getId(), updateOutlets);
        }
    }

    @Override
    public void checkMerchantPhone(String merchantType, String merchantId, String phone) throws ApiException {
        Query query = Query.query(Criteria.where("phone").is(phone).and("deleteFlag").is(false));
        BaseMerchant baseMerchant = null;
        switch (merchantType) {
            case Constants.MerchantType.FACTORY:
                baseMerchant = factoryDao.findOne(query);
                break;
            case Constants.MerchantType.SERVICE_PROVIDER:
                baseMerchant = serviceProviderDao.findOne(query);
                break;
            case Constants.MerchantType.MECHANIC:
                baseMerchant = mechanicDao.findOne(query);
                break;
            case Constants.MerchantType.OUTLETS:
                baseMerchant = outletsDao.findOne(query);
                break;
            default:
                throw new ApiException("商户类型非法");
        }
        if (StringUtils.isEmpty(merchantId)) {
            //merchantId为null 是新增商户
            if (null != baseMerchant) {
                throw new ApiException("该手机号码已存在");
            }
        } else {
            //手机号码存在且id不是自己的id，则抛出异常
            if (baseMerchant != null && !StringUtils.equals(baseMerchant.getId(), merchantId)) {
                throw new ApiException("该手机号码已存在");
            }
        }
    }

    @Override
    public Map<String, Object> getMerchantAccount() throws ApiException {
        String merchantType = LoginUtil.getLoginType();
        String merchantId = LoginUtil.getLoginId();
        BaseMerchant merchantInfo = getMerchantInfo(merchantType, merchantId);
        if (null == merchantInfo) {
            throw new ApiException("账号异常，请联系autoask");
        }
        Example example = new Example(MerchantAssets.class);
        example.createCriteria().andEqualTo("merchantType", merchantType).andEqualTo("merchantId", merchantId);
        List<MerchantAssets> merchantAssetsList = merchantAssetsMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(merchantAssetsList)) {
            throw new ApiException("账号异常，请联系autoask");
        }
        MerchantAssets merchantAssets = merchantAssetsList.get(0);
        Map<String, Object> resultMap = new HashedMap();
        resultMap.put("balance", merchantAssets.getBalance());
        resultMap.put("aliPayAccount", merchantInfo.getAliPayAccount());
        resultMap.put("aliPayUserName", merchantInfo.getAliPayUserName());
        return resultMap;
    }

    @Override
    public void updateMerchantAliAccount(String aliPayAccount, String aliPayUserName) throws ApiException {
        if (StringUtils.isEmpty(aliPayAccount)) {
            throw new ApiException("支付宝账号不能为空");
        }
        if (StringUtils.isEmpty(aliPayUserName)) {
            throw new ApiException("支付宝收款人姓名不能为空");
        }
        String merchantType = LoginUtil.getLoginType();
        String merchantId = LoginUtil.getLoginId();
        Update update = new Update();
        update.set("aliPayAccount", aliPayAccount);
        update.set("aliPayUserName", aliPayUserName);

        switch (merchantType) {
            case Constants.MerchantType.FACTORY:
                factoryDao.update(merchantId, update);
                break;
            case Constants.MerchantType.SERVICE_PROVIDER:
                serviceProviderDao.update(merchantId, update);
                break;
        }
    }

    @Override
    public void updateMerchantPassword(String password) throws ApiException {
        if (StringUtils.isEmpty(password)) {
            throw new ApiException("密码不能为空");
        }
        String loginType = LoginUtil.getLoginType();
        String loginId = LoginUtil.getLoginId();
        Update update = new Update();
        update.set("password", password);
        switch (loginType) {
            case Constants.LoginType.STAFF:
                staffDao.update(loginId, update);
                break;
            case Constants.LoginType.FACTORY:
                factoryDao.update(loginId, update);
                break;
            case Constants.LoginType.SERVICE_PROVIDER:
                serviceProviderDao.update(loginId, update);
                break;
        }
    }

    @Override
    public void updateMerchantVatInvoice(VatInvoice vatInvoice) throws ApiException {
        String loginType = LoginUtil.getLoginType();
        String loginId = LoginUtil.getLoginId();
        switch (loginType) {
            case Constants.MerchantType.FACTORY:
                Factory factory = factoryDao.findById(loginId);
                if (null == factory) {
                    throw new ApiException("帐号不存在");
                }
                factory.setVatInvoice(vatInvoice);
                factoryDao.updateSelective(factory.getId(), factory);
                break;
            case Constants.MerchantType.SERVICE_PROVIDER:
                ServiceProvider serviceProvider = serviceProviderDao.findById(loginId);
                if (null == serviceProvider) {
                    throw new ApiException("帐号不存在");
                }
                serviceProvider.setVatInvoice(vatInvoice);
                serviceProviderDao.updateSelective(serviceProvider.getId(), serviceProvider);
                break;
        }
    }

    @Override
    public VatInvoice getMerchantVatInvoice() throws ApiException {
        String loginType = LoginUtil.getLoginType();
        String loginId = LoginUtil.getLoginId();
        VatInvoice vatInvoice = null;
        switch (loginType) {
            case Constants.MerchantType.FACTORY:
                Factory factory = factoryDao.findById(loginId);
                if (null == factory) {
                    throw new ApiException("帐号不存在");
                }
                vatInvoice = factory.getVatInvoice();
                break;
            case Constants.MerchantType.SERVICE_PROVIDER:
                ServiceProvider serviceProvider = serviceProviderDao.findById(loginId);
                if (null == serviceProvider) {
                    throw new ApiException("帐号不存在");
                }
                vatInvoice = serviceProvider.getVatInvoice();
                break;
        }
        return vatInvoice;
    }

    @Override
    public String getSelfQRCode() throws ApiException {
        String merchantType = LoginUtil.getLoginType();
        String merchantId = LoginUtil.getLoginId();
        QRCode qrCode = qrCodeService.getQRCode(merchantType, merchantId);
        if (null == qrCode) {
            return null;
        }
        return qrCode.getCode();
    }

    @Override
    public void updateSelfQRCode(String code) throws ApiException {
        String merchantType = LoginUtil.getLoginType();
        String merchantId = LoginUtil.getLoginId();
        qrCodeService.bindQRCode(code, merchantType, merchantId);
    }
}
