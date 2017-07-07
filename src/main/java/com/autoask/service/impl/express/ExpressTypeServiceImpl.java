package com.autoask.service.impl.express;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.express.ExpressTypeDao;
import com.autoask.entity.mongo.express.ExpressType;
import com.autoask.service.express.ExpressTypeService;
/**
 * 快递类型
 * @author ck
 *
 * @Create 2017年4月1日下午3:00:27
 */
@Service("expressTypeService")
public class ExpressTypeServiceImpl implements ExpressTypeService {
	private static final Logger LOG = LoggerFactory.getLogger(ExpressTypeServiceImpl.class);
	@Autowired
	private ExpressTypeDao expressTypeDao;
	
	@Override
	public ExpressType getById(String id) throws ApiException {
		 LOG.info("get ExpressType by id ");
	        try {
	             return expressTypeDao.findOne(Query.query(Criteria.where("id").is(id).and("deleteFlag").is(false)).with(new Sort(Direction.ASC, "create_time")));
	        } catch (Exception e) {
	            LOG.warn(e.getMessage());
	            throw new ApiException(e.getMessage());
	        }
	}

	@Override
	public ListSlice<ExpressType> getExpressTypeList(String cnName, String ukName, Integer start, Integer limit)
			throws ApiException {
		LOG.info("getList getExpressTypeList by cnName and ukName");
        try {
        	 Criteria criteria = new Criteria();
             criteria.and("deleteFlag").is(false);
             //查询中文名称
             if (StringUtils.isNotEmpty(cnName)) {
                 criteria.and("cnName").is(cnName);
             }
             //模糊查询英文名称
             if (StringUtils.isNotEmpty(ukName)) {
                 String nameReg = ".*" + ukName + ".*";
                 criteria.and("ukName").regex(nameReg);
             }
             Query query = Query.query(criteria);
             if (null != start) {
                 query.skip(start);
             }
             if (null != limit) {
                 query.limit(limit);
             }
        	List<ExpressType> staffList = expressTypeDao.find(query);
            long totalNum = expressTypeDao.count(Query.query(criteria));
            return new ListSlice<>(staffList, totalNum);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
		}
	}

	@Override
	public ListSlice<ExpressType> getNotPageExpressTypeList() throws ApiException {
		LOG.info("getList getNotPageExpressTypeList by cnName and ukName");
        try {
        	Criteria criteria = new Criteria();
        	criteria.and("deleteFlag").is(false);
            Query query = Query.query(criteria);
        	List<ExpressType> staffList = expressTypeDao.find(query);
            long totalNum = expressTypeDao.count(Query.query(criteria));
            return new ListSlice<>(staffList, totalNum);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
		}
	}

	@Override
	public void saveExpressType(ExpressType expressType) throws ApiException {
		LOG.info("save ExpressType");
		try {
			//判断是否有未填信息或者错误字段
			whetherErr(expressType);
			//保存公共字段
			expressType.setId(null);
			expressType.setDeleteFlag(false);
			expressType.setCreateTime(DateUtil.getDate());
			expressType.setCreatorId(LoginUtil.getLoginId());
			expressType.setCreatorType(LoginUtil.getLoginType());
			expressTypeDao.save(expressType);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
		}
	}

	@Override
	public void updateExpressType(ExpressType expressType) throws ApiException {
		LOG.info("update ExpressType");
		try {
			if(StringUtils.isEmpty(expressType.getId())){
				throw new ApiException("id参数错误");
			}
			ExpressType expressOne=expressTypeDao.findOne(Query.query(Criteria.where("id").is(expressType.getId()).and("deleteFlag").is(false)));
			if(expressOne == null){
				throw new ApiException("未查询到数据");
			}
			expressTypeDao.updateSelective(expressType.getId(), expressType);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
		}
	}

	@Override
	public void deleteExpressTypeById(String id) throws ApiException {
		LOG.info("delete ExpressType by id");
		try {
			ExpressType expressOne=expressTypeDao.findOne(Query.query(Criteria.where("id").is(id).and("deleteFlag").is(false)));
			if(expressOne==null){
				throw new ApiException("未查询到数据");
			}
			ExpressType expressType = new ExpressType();
			expressType.setDeleteFlag(true);
			expressType.setDeleteId(LoginUtil.getLoginId());
			expressType.setDeleteType(LoginUtil.getLoginType());
			expressType.setDeleteTime(DateUtil.getDate());
			expressTypeDao.updateSelective(id, expressType);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
		}
	}
	
	/**
	 * 判断为空或者字段错误
	 * @param expressType
	 * @throws ApiException
	 */
	public static void whetherErr(ExpressType expressType) throws ApiException{
        if (expressType == null) {
            throw new ApiException("对象不能为空");
        }
        if (StringUtils.isEmpty(expressType.getCnName())) {
            throw new ApiException("快递中文名不能为空");
        }
        if (StringUtils.isEmpty(expressType.getUkName())) {
            throw new ApiException("快递英文名不能为空");
        }
	}

}
