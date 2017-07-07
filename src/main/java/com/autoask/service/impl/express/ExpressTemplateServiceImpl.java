package com.autoask.service.impl.express;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.BigDecimalUtil;
import com.autoask.common.util.CheckBoxUtil;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.express.ExpressTemplateDao;
import com.autoask.dao.express.ExpressTypeDao;
import com.autoask.entity.mongo.express.ExpressTemplate;
import com.autoask.entity.mongo.express.ExpressType;
import com.autoask.entity.mysql.CityArea;
import com.autoask.service.common.CityAreaService;
import com.autoask.service.config.ConfigService;
import com.autoask.service.express.ExpressTemplateService;
/**
 * 快递模板信息
 * @author ck
 *
 * @Create 2017年4月1日上午8:46:16
 */
@Service("expressTemplateService")
public class ExpressTemplateServiceImpl implements ExpressTemplateService {
	private static final Logger LOG = LoggerFactory.getLogger(ExpressTemplateServiceImpl.class);
	@Autowired
	ExpressTemplateDao expressTemplateDao;
	
	@Autowired
	ConfigService configService;
	
	@Autowired
	CityAreaService cityAreaService;
	
	@Autowired
	private ExpressTypeDao expressTypeDao;
	
	@Override
	public ExpressTemplate getById(String id) throws ApiException {
        LOG.info("get expressTemplate by id ");
        try {
             return expressTemplateDao.findOne(Query.query(Criteria.where("id").is(id).and("deleteFlag").is(false)));
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
	}

	@Override
	public ListSlice<ExpressTemplate> getExpressTemplateList(String name,String expressTypeId,Integer start, Integer limit) throws ApiException {
		LOG.info("getList getExpressTemplateList by name and expressType");
        try {
        	 Criteria criteria = new Criteria();
             criteria.and("deleteFlag").is(false);
             //查询模板类型
             if (StringUtils.isNotEmpty(expressTypeId)) {
                 criteria.and("expressTypeId").is(expressTypeId);
             }
             //模糊查询模板名称
             if (StringUtils.isNotEmpty(name)) {
                 String nameReg = ".*" + name + ".*";
                 criteria.and("name").regex(nameReg);
             }
             Query query = Query.query(criteria);
             if (null != start) {
                 query.skip(start);
             }
             if (null != limit) {
                 query.limit(limit);
             }
        	List<ExpressTemplate> staffList = expressTemplateDao.find(query);
            long totalNum = expressTemplateDao.count(Query.query(criteria));
            return new ListSlice<>(staffList, totalNum);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
		}
	}

	@Override
	public void saveExpressTemplate(ExpressTemplate expressTemplate) throws ApiException {
		LOG.info("save ExpressTemplate");
		try {
			//判断是否有未填信息或者错误字段
			whetherErr(expressTemplate);
			ExpressType expressTypeOne = expressTypeDao.findOne(Query.query(Criteria.where("id").is(expressTemplate.getExpressTypeId()).and("deleteFlag").is(false)));
			if(expressTypeOne == null){
				throw new ApiException("未查询到此快递信息");
			}
			isProvinceRepeat(expressTemplate.getRange(),0,null);
			expressTemplate.setId(null);
			expressTemplate.setExpressTypeName(expressTypeOne.getCnName());
			expressTemplate.setDeleteFlag(false);
			expressTemplate.setCreateTime(DateUtil.getDate());
			expressTemplate.setCreatorId(LoginUtil.getLoginId());
			expressTemplate.setCreatorType(LoginUtil.getLoginType());
			expressTemplateDao.save(expressTemplate);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
		}
	}

	@Override
	public void updateExpressTemplate(ExpressTemplate expressTemplate) throws ApiException {
		LOG.info("update ExpressTemplate");
		try {
			ExpressTemplate expressOne=expressTemplateDao.findOne(Query.query(Criteria.where("id").is(expressTemplate.getId()).and("deleteFlag").is(false)));
			if(expressOne==null){
				throw new ApiException("id错误");
			}
			ExpressType expressTypeOne = expressTypeDao.findOne(Query.query(Criteria.where("id").is(expressTemplate.getExpressTypeId()).and("deleteFlag").is(false)));
			if(expressTypeOne == null){
				throw new ApiException("未查询到此快递信息");
			}
			isProvinceRepeat(expressTemplate.getRange(), 1, expressTemplate.getId());
			expressTemplate.setExpressTypeName(expressTypeOne.getCnName());
			expressTemplateDao.updateSelective(expressTemplate.getId(), expressTemplate);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
		}
	}

	@Override
	public void deleteExpressTemplateById(String id) throws ApiException {
		LOG.info("delete ExpressTemplate by id");
		try {
			ExpressTemplate expressOne=expressTemplateDao.findOne(Query.query(Criteria.where("id").is(id).and("deleteFlag").is(false)));
			if(expressOne==null){
				throw new ApiException("id参数错误");
			}
			ExpressTemplate expressTemplate = new ExpressTemplate();
			expressTemplate.setDeleteFlag(true);
			expressTemplate.setDeleteId(LoginUtil.getLoginId());
			expressTemplate.setDeleteType(LoginUtil.getLoginType());
			expressTemplate.setDeleteTime(DateUtil.getDate());
			expressTemplateDao.updateSelective(id, expressTemplate);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
		}
	}
	
	@Override
	public Map<String, BigDecimal> getOriginalPrice(String price, String province, String totalWeight) throws ApiException {
		//返回的快递件价格
		Map<String,BigDecimal> map = new HashedMap(1,2);
		BigDecimal expressPrice = new BigDecimal(0);
		BigDecimal lookExpressPice = new BigDecimal(0);
		if(StringUtils.isEmpty(price)){
			throw new ApiException("价格不能为空");
		}if(StringUtils.isEmpty(province)){
			throw new ApiException("地区不能为空");
		}if(StringUtils.isEmpty(totalWeight)){
			throw new ApiException("快递重量不能为空");
		}if(!isNumeric(price)){
			throw new ApiException("请输入正确价格(最大2位小数且大于0的正数)");
		}if(!isNumeric(totalWeight)){
			throw new ApiException("请输入正确快递重量(最大2位小数且大于0的正数)");
		}
		//根据地区查询快递模板信息
	   	Criteria criteria = new Criteria();
	    criteria.and("deleteFlag").is(false);
	    Query query = Query.query(criteria);
    	List<ExpressTemplate> expressTemplateList = expressTemplateDao.find(query);
    	lookExpressPice = getPice(expressTemplateList,province,totalWeight);
    	map.put("originalPice", lookExpressPice.setScale(2, RoundingMode.HALF_UP));
		//根据地区查询查询免邮价格
		BigDecimal mailFreePrice=new BigDecimal(configService.getByProvince(province));
		//返回价格必须大于0,等于0则不是免邮地区。商品价格大于等于免邮设置价格则不需要计算快递价格
		if(mailFreePrice.signum() == 1 && new BigDecimal(price).compareTo(mailFreePrice) == 1 
				|| mailFreePrice.signum() == 1 && new BigDecimal(price).compareTo(mailFreePrice) == 0){
				expressPrice = new BigDecimal(0.00);
				map.put("expressPrice", expressPrice.setScale(2, RoundingMode.HALF_UP));
		}else{
			if(expressTemplateList == null || expressTemplateList.size()<=0){
				throw new ApiException("未查询到此快递类型模板");
			}else{
				expressPrice = getPice(expressTemplateList,province,totalWeight);
		}
		 
		 map.put("expressPrice", expressPrice.setScale(2, RoundingMode.HALF_UP));
	}
		return map;
}
	
	@Override
	public BigDecimal getExpressPrice(String price, String province, String totalWeight) throws ApiException {
		//返回的快递件价格
		BigDecimal expressPrice = new BigDecimal(0);
		if(StringUtils.isEmpty(price)){
			throw new ApiException("价格不能为空");
		}if(StringUtils.isEmpty(province)){
			throw new ApiException("地区不能为空");
		}if(StringUtils.isEmpty(totalWeight)){
			throw new ApiException("快递重量不能为空");
		}if(!isNumeric(price)){
			throw new ApiException("请输入正确价格(最大2位小数且大于0的正数)");
		}if(!isNumeric(totalWeight)){
			throw new ApiException("请输入正确快递重量(最大2位小数且大于0的正数)");
		}
		//根据地区查询查询免邮价格
		BigDecimal mailFreePrice=new BigDecimal(configService.getByProvince(province));
		//返回价格必须大于0,等于0则不是免邮地区。商品价格大于等于免邮设置价格则不需要计算快递价格
		if(mailFreePrice.signum() == 1 && new BigDecimal(price).compareTo(mailFreePrice) == 1 
				|| mailFreePrice.signum() == 1 && new BigDecimal(price).compareTo(mailFreePrice) == 0){
				expressPrice = new BigDecimal(0.00);
		}else{
			//根据地区查询快递模板信息
		   	Criteria criteria = new Criteria();
		    criteria.and("deleteFlag").is(false);
		    Query query = Query.query(criteria);
	    	List<ExpressTemplate> expressTemplateList = expressTemplateDao.find(query);
			if(expressTemplateList == null || expressTemplateList.size()<=0){
				throw new ApiException("未查询到此快递类型模板");
			}else{
				expressPrice = getPice(expressTemplateList,province,totalWeight);
			}
		}
		return expressPrice.setScale(2, RoundingMode.HALF_UP);
	}
	
	public static BigDecimal getPice(List<ExpressTemplate> expressTemplateList,String province,String totalWeight) throws ApiException{
		List<ExpressTemplate> expressList = new ArrayList<ExpressTemplate>();
		for (ExpressTemplate item : expressTemplateList) {
			String[] range = item.getRange().split(",");
			//数组转换成list
			List<String> list=Arrays.asList(range);
			//判断是否存在地区
			if(list.contains(province)){
				expressList.add(item);
			}
		}
		if(expressList.size() > 1){
			throw new ApiException("数据大于1条，数据条目数错误");
		}
		if(expressList.size() < 1){
			throw new ApiException("未查询到此地区信息");
		}
		//首重
		BigDecimal firstHeavy = expressList.get(0).getFirstHeavy();
		//首重价格
		BigDecimal firstHeavyPrice = expressList.get(0).getFirstHeavyPrice();
		//续重
		BigDecimal continuedHeavy = expressList.get(0).getContinuedHeavy();
		//续重单价
		BigDecimal continuedHeavyPrice = expressList.get(0).getContinuedHeavyPrice();
		//快递重量大于首重
		if(new BigDecimal(totalWeight).compareTo(firstHeavy) == 1){
			//快递实际续重
			BigDecimal actualContinuedHeavy = BigDecimalUtil.sub(new BigDecimal(totalWeight), firstHeavy);
			//得到续重单价
			BigDecimal actualContinuedHeavyPrice = BigDecimalUtil.mul(BigDecimalUtil.div(continuedHeavyPrice, continuedHeavy, 2), actualContinuedHeavy);
			//快递实际价格
			return  BigDecimalUtil.add(firstHeavyPrice, actualContinuedHeavyPrice);
		}else{
			return  firstHeavyPrice;
		}
	}
	
	/**
	 * 判断是否为正确数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
	   Pattern pattern = Pattern.compile("^\\d+(.\\d{1,2})?$"); 
	   Matcher isNum = pattern.matcher(str);
	   if( !isNum.matches() ){
	       return false; 
	   } 
	   return true; 
	}
	
	/**
	 * 判断是否有未填信息或者错误字段
	 * @param expressTemplate
	 * @return
	 * @throws ApiException
	 */
	public static void whetherErr(ExpressTemplate expressTemplate) throws ApiException{
        if (expressTemplate == null) {
            throw new ApiException("对象不能为空");
        }
        if (StringUtils.isEmpty(expressTemplate.getName())) {
            throw new ApiException("模板名称不能为空");
        }
        if (StringUtils.isEmpty(expressTemplate.getExpressTypeId())) {
            throw new ApiException("快递类型不能为空");
        }
        if (StringUtils.isEmpty(expressTemplate.getRange())) {
            throw new ApiException("运送范围不能为空");
        }
        if(expressTemplate.getFirstHeavy()==null){
        	throw new ApiException("首重质量不能为空");
        }
        if(expressTemplate.getFirstHeavyPrice()==null){
        	throw new ApiException("首重价格不能为空");
        }
        if(expressTemplate.getContinuedHeavy()==null){
        	throw new ApiException("续重单质量不能为空");
        }
        if(expressTemplate.getContinuedHeavyPrice()==null){
        	throw new ApiException("续重单价不能为空");
        }
	}

	@Override
	public List<CheckBoxUtil> getTemplateProvinceList(String id) throws ApiException {
		List<CheckBoxUtil> checkBox = new ArrayList<CheckBoxUtil>();
		//查询所有省
		List<CityArea> cityAreaList = cityAreaService.getCityAreaByParentId(0);
		try {
			if(!StringUtils.isEmpty(id)){
				//根据id查询
				ExpressTemplate expressOne=expressTemplateDao.findOne(Query.query(Criteria.where("id").is(id).and("deleteFlag").is(false)));
				if(expressOne==null){
					throw new ApiException("id参数错误");
				}
				//判断是否有地区数据
				if(!StringUtils.isEmpty(expressOne.getRange())){
					//转换成数组的格式
					String [] deliveryArray = expressOne.getRange().split(",");
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
	public void isProvinceRepeat(String province, int type, String id) throws ApiException {
		//根据地区查询快递模板信息
	   	Criteria criteria = new Criteria();
	    criteria.and("deleteFlag").is(false);
	    Query query = Query.query(criteria);
    	List<ExpressTemplate> expressTemplateList = expressTemplateDao.find(query);
		for (ExpressTemplate item : expressTemplateList) {
			String[] range = item.getRange().split(",");
			//数组转换成list
			List<String> list=Arrays.asList(range);
			if(type == 1){
				if(!id.equals(item.getId())){
					if(StringUtils.isEmpty(province)){
						throw new ApiException("地区不能为空！");
					}else{
						String [] arry = province.split(",");
						for(int i = 0;i<arry.length;i++){
							//判断是否存在地区
							if(list.contains(arry[i])){
								throw new ApiException("所选的("+arry[i]+")地区已经重复");
							}
						}
					}
				}
			}else{
				if(StringUtils.isEmpty(province)){
					throw new ApiException("地区不能为空！");
				}else{
					String [] arry = province.split(",");
					for(int i = 0;i<arry.length;i++){
						//判断是否存在地区
						if(list.contains(arry[i])){
							throw new ApiException("所选的("+arry[i]+")地区已经重复");
						}
					}
				}
			}
		}
	}
}
