package com.autoask.service.impl.weiixn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.entity.mysql.WeiXinUserInfo;
import com.autoask.mapper.WeiXinUserInfoMapper;
import com.autoask.service.weiixn.WeiXinUserInfoService;
/**
 *微信用户业务实现类
 * @author ck
 *
 * @Create 2017年6月14日下午3:25:27
 */
@Service("weiXinUserInfoService")
public class WeiXinUserInfoServiceImpl implements WeiXinUserInfoService {
	private static Logger LOG = LoggerFactory.getLogger(WeiXinUserInfoService.class);
	
	@Autowired
	WeiXinUserInfoMapper weiXinUserInfoMapper;

	@Override
	public WeiXinUserInfo selectWeiXinUserInfoById(String openId) throws ApiException {
		return weiXinUserInfoMapper.selectWeiXinUserInfoById(openId);
	}

	@Override
	public ListSlice<WeiXinUserInfo> selectWeiXinUserInfoList(String nickName, Integer start, Integer limit)
			throws ApiException {
		List<WeiXinUserInfo> weiXinUserInfoList = new ArrayList<>();
		Long num = new Long(0);
		try {
			weiXinUserInfoList = weiXinUserInfoMapper.selectWeiXinUserInfoList(nickName, false, start, limit);
			if(weiXinUserInfoList.size() > 0 && weiXinUserInfoList!=null){
				for (int i = 0; i < weiXinUserInfoList.size(); i++) {
					//判断是否关注
					if(weiXinUserInfoList.get(i).getSubscribe() == 0L){
						weiXinUserInfoList.get(i).setSubscribeName("未关注");
					} else {
						weiXinUserInfoList.get(i).setSubscribeName("关注");
					}
					//判断性别
					if(weiXinUserInfoList.get(i).getSex() == 1L){
						weiXinUserInfoList.get(i).setSexName("男");
					}else if(weiXinUserInfoList.get(i).getSex() == 2L){
						weiXinUserInfoList.get(i).setSexName("女");
					}else{
						weiXinUserInfoList.get(i).setSexName("未知");
					}
				}
			}
			num = weiXinUserInfoMapper.selectWeiXinUserInfoNum(nickName);
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
		return new ListSlice(weiXinUserInfoList, num);
	}

	@Override
	public void saveWeiXinUserInfo(WeiXinUserInfo weiXinUserInfo) throws ApiException {
		try {
			if(StringUtils.isEmpty(weiXinUserInfo.getOpenId())){
				throw new ApiException("openId不能为空！");
			}
			WeiXinUserInfo userInfo = weiXinUserInfoMapper.selectWeiXinUserInfoById(weiXinUserInfo.getOpenId());
			if(userInfo == null){
				weiXinUserInfo.setCreateTime(DateUtil.getDate());
				weiXinUserInfo.setDeleteFlag(false);
				weiXinUserInfoMapper.insert(weiXinUserInfo);
			}else{
				throw new ApiException("微信用户已经添加！");
			}
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
	}

	@Override
	public void cancelAttention(String openId) throws ApiException {
		try {
			weiXinUserInfoMapper.cancelAttention(openId);
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
	}

	@Override
	public void subscribe(String openId) throws ApiException {
		try {
			weiXinUserInfoMapper.subscribe(openId);
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
	}

}
