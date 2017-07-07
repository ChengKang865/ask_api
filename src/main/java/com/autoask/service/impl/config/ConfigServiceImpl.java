package com.autoask.service.impl.config;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CheckBoxUtil;
import com.autoask.dao.config.BannerDao;
import com.autoask.dao.config.DeliveryInfoDao;
import com.autoask.entity.mongo.config.Banner;
import com.autoask.entity.mongo.config.DeliveryInfo;
import com.autoask.entity.mysql.CityArea;
import com.autoask.service.common.CityAreaService;
import com.autoask.service.config.ConfigService;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author hyy
 * @create 16/10/23 20:56
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    //TODO 全局设置 set 应该 在写入mongodb中再写入redis中
    //读取应该先从 redis中读取 在从 mongodb中读取

    @Autowired
    private DeliveryInfoDao deliveryInfoDao;

    @Autowired
    private BannerDao bannerDao;
    
    @Autowired
    private CityAreaService cityAreaService;

    /**
     * 获取免邮的价格
     *
     * @return
     */
    @Override
    public long getDeliveryFreeAmount() {
        DeliveryInfo freeAmount = deliveryInfoDao.findOne(Query.query(Criteria.where("freeAmount").exists(true)));
        if (null == freeAmount) {
            DeliveryInfo deliveryInfo = new DeliveryInfo();
            deliveryInfo.setFreeAmount(0L);
            deliveryInfoDao.save(deliveryInfo);
            return 0L;
        }
        return freeAmount.getFreeAmount()/100;
    }

	@Override
    public void updateDeliveryFreeAmount(Long amount,String province) throws ApiException {
        if (null == amount) {
            throw new ApiException("额度不能为空");
        }
        if (amount < 0L) {
            throw new ApiException("额度不能为负数");
        }
        DeliveryInfo preFree = deliveryInfoDao.findOne(Query.query(Criteria.where("freeAmount").exists(true)));
        Long FreeAmount =amount*100;
        if (null == preFree) {
            DeliveryInfo deliveryInfo = new DeliveryInfo();
            preFree.setFreeAmount(FreeAmount);
            preFree.setProvince(province);
            deliveryInfoDao.save(deliveryInfo);
        }
        preFree.setFreeAmount(FreeAmount);
        preFree.setProvince(province);
        deliveryInfoDao.updateSelective(preFree.getId(), preFree);
    }

    /**
     * 获取渠道banner图
     *
     * @param channel
     * @return
     * @throws ApiException
     */
    @Override
    public List<Banner> getBannerList(String channel) throws ApiException {
//        if (!Constants.BannerChannel.CHANNEL_LIST.contains(channel)) {
//            throw new ApiException("渠道参数非法");
//        }
        return bannerDao.getBannerList(channel);
    }

    /**
     * 新增 更新 banner list
     *
     * @param bannerList
     * @throws ApiException
     */
    @Override
    public List<Banner> insertOrUpdateBannerList(List<Banner> bannerList, String channel) throws ApiException {
        return bannerDao.insertOrUpdateBannerList(bannerList, channel);
    }

    @Override
    public void setAdFeeCircleDistance(Long distance) throws ApiException {

    }
    
	@Override
	public int getByProvince(String province) throws ApiException {
		try {
			List<DeliveryInfo> deliveryInfoList=deliveryInfoDao.listAll();
			if(deliveryInfoList.size() == 1){
				Boolean isFree = deliveryInfoList.get(0).getProvince().contains(province);
				if(isFree){
					return 0;
				}else{
					return (int) (deliveryInfoList.get(0).getFreeAmount()/100);
				}
			}else{
				throw new ApiException("数据条目数错误");
			}
		} catch (Exception e) {
            throw new ApiException(e.getMessage());
		}
	}

	@Override
	public List<CheckBoxUtil> provinceList() throws ApiException{
		List<CheckBoxUtil> checkBox = new ArrayList<CheckBoxUtil>();
		try {
			//查询免邮信息
			List<DeliveryInfo> deliveryInfoList=deliveryInfoDao.listAll();
			//查询所有省
			List<CityArea> cityAreaList = cityAreaService.getCityAreaByParentId(0);
			if(deliveryInfoList.size() != 1){
				throw new ApiException("数据条目数不正确");
			}
			//获取地区
			String delivery = deliveryInfoList.get(0).getProvince();
			if(!StringUtils.isEmpty(delivery)){
				//转换成数组的格式
				String [] deliveryArray = delivery.split(",");
				List<String> list=Arrays.asList(deliveryArray);
				if (cityAreaList.size() > 0 && cityAreaList != null) {
					for (int i = 0; i < cityAreaList.size(); i++) {
						CheckBoxUtil checkBoxUtil =new CheckBoxUtil();
						checkBoxUtil.setId(cityAreaList.get(i).getId());
						checkBoxUtil.setName(cityAreaList.get(i).getAreaName());
						//地区匹配成功
						if(list.contains(cityAreaList.get(i).getAreaName())){
							checkBoxUtil.setIsChoice(true);
						}else{
							checkBoxUtil.setIsChoice(false);
						}
						checkBox.add(i, checkBoxUtil);
					}
				} else {
					throw new ApiException("未查询到地区数据");
				}
			}else{
				for (int i = 0; i < cityAreaList.size(); i++) {
					CheckBoxUtil checkBoxUtil =new CheckBoxUtil();
					checkBoxUtil.setId(cityAreaList.get(i).getId());
					checkBoxUtil.setName(cityAreaList.get(i).getAreaName());
					checkBoxUtil.setIsChoice(false);
					checkBox.add(i, checkBoxUtil);
				}
			}
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
		return checkBox;
	}

	@Override
	public Map provinceName() throws ApiException {
		Map map = new HashedMap(1,2);
		try {
			List<DeliveryInfo> deliveryInfoList=deliveryInfoDao.listAll();
			map.put("province", deliveryInfoList.get(0).getProvince());
			map.put("freeAmount", deliveryInfoList.get(0).getFreeAmount()/100);
		} catch (Exception e) {
            throw new ApiException(e.getMessage());
		}
		return map;
	}
}
