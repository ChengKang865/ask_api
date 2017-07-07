package com.autoask.service.impl.express;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.express.MailFreeDao;
import com.autoask.entity.mongo.express.MailFree;
import com.autoask.service.express.MailFreeService;

/**
 * 免邮设置实现类
 * @author ck
 *
 * @Create 2017年4月8日上午10:01:20
 */
@Service("mailFreeService")
public class MailFreeServiceImpl implements MailFreeService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MailFreeServiceImpl.class);
	
	@Autowired
	MailFreeDao mailFreeDao;

	@Override
	public String getByProvince(String province) throws ApiException {
		LOG.info("get price by province");
		try {
			List<MailFree> mailFreeListAll=mailFreeDao.listAll();
			if(mailFreeListAll.size() == 1){
				Boolean isFree = mailFreeListAll.get(0).getProvince().contains(province);
				if(isFree){
					return "0";
				}else{
					return mailFreeListAll.get(0).getPrice().toString();
				}
			}else{
				throw new ApiException("数据条目数错误");
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
		}
	}

	@Override
	public void saveMailFree(MailFree mailFree) throws ApiException {
		LOG.info("save MailFree");
		try {
			//判断是否有未填信息或者错误字段
			if(StringUtils.isEmpty(mailFree.getProvince())){
				throw new ApiException("地区不能为空");
			}if(mailFree.getPrice() != null){
				throw new ApiException("免邮价格不能为空");
			}
			mailFree.setId(null);
			mailFree.setDeleteFlag(false);
			mailFree.setCreateTime(DateUtil.getDate());
			mailFree.setCreatorId(LoginUtil.getLoginId());
			mailFree.setCreatorType(LoginUtil.getLoginType());
			mailFreeDao.save(mailFree);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
		}
	}

	@Override
	public void updateMailFree(MailFree mailfree) throws ApiException {
		LOG.info("update MailFree");
		try {
			if(StringUtils.isEmpty(mailfree.getId())){
				throw new ApiException("id参数错误");
			}
			MailFree mailFree=mailFreeDao.findOne(Query.query(Criteria.where("id").is(mailfree.getId()).and("deleteFlag").is(false)));
			if(mailFree==null){
				throw new ApiException("未查询到数据");
			}
			mailFreeDao.updateSelective(mailfree.getId(), mailFree);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
		}
	}

}
