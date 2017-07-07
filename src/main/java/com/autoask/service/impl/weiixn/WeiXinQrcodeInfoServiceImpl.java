package com.autoask.service.impl.weiixn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autoask.cache.AccessTokenService;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CodeGenerator;
import com.autoask.common.util.DateUtil;
import com.autoask.common.util.ListSlice;
import com.autoask.common.util.weixin.WeixinUtil;
import com.autoask.entity.common.weiixn.AccessToken;
import com.autoask.entity.common.weiixn.WeiXinQRCode;
import com.autoask.entity.mysql.WeiXinQrcodeInfo;
import com.autoask.mapper.WeiXinQrcodeInfoMapper;
import com.autoask.pay.ali.util.UtilDate;
import com.autoask.service.weiixn.WeiXinQrcodeInfoService;

import tk.mybatis.mapper.entity.Example;

/**
 * 微信二维码关注来源业务实现类
 * 
 * @author ck
 *
 * @Create 2017年6月9日上午10:08:17
 */
@Service("weiXinQrcodeInfoService")
public class WeiXinQrcodeInfoServiceImpl implements WeiXinQrcodeInfoService {
	private static Logger LOG = LoggerFactory.getLogger(WeiXinQrcodeInfoServiceImpl.class);

	@Autowired
	AccessTokenService accessTokenService;
	@Autowired
	WeiXinQrcodeInfoMapper weiXinQrcodeInfoMapper;

	@Override
	public WeiXinQrcodeInfo getWeiXinQrcodeInfoById(String id) throws ApiException {
		return weiXinQrcodeInfoMapper.selectWeiXinQrcodeInfoById(id);
	}

	@Override
	public ListSlice<WeiXinQrcodeInfo> getWeiXinQrcodeInfoList(String name, Integer start, Integer limit)
			throws ApiException {
		List<WeiXinQrcodeInfo> weiXinQrcodeInfoList = new ArrayList<>();
		Long num = new Long(0);
		// 订阅总数
		Long count = weiXinQrcodeInfoMapper.countSubscribeCount();
		try {
			weiXinQrcodeInfoList = weiXinQrcodeInfoMapper.selectWeiXinQrcodeInfoList(name, false, start, limit);
			if (weiXinQrcodeInfoList != null && weiXinQrcodeInfoList.size() != 0) {
				for (int i = 0; i < weiXinQrcodeInfoList.size(); i++) {
					// 订阅占比
					String subscribePercentage = UtilDate.myPercent(weiXinQrcodeInfoList.get(i).getSubscribeCount(),
							count);
					weiXinQrcodeInfoList.get(i).setSubscribePercentage(subscribePercentage);
					//二维码地址
					if(!StringUtils.isEmpty(weiXinQrcodeInfoList.get(i).getTicket())){
						weiXinQrcodeInfoList.get(i).setImgUrl(WeixinUtil.SHOW_QRCODE.replace("TICKET", weiXinQrcodeInfoList.get(i).getTicket()));
					}else{
						weiXinQrcodeInfoList.get(i).setImgUrl(WeixinUtil.NOT_IMG);
					}
					// 二维码类型
					String type = weiXinQrcodeInfoList.get(i).getType();
					if (StringUtils.equals(WeixinUtil.QR_SCENE, type)) {
						weiXinQrcodeInfoList.get(i).setTypeName(WeixinUtil.TEMPORARY);
						//判断是否失效
						Long time = DateUtil.timeDifference(weiXinQrcodeInfoList.get(i).getCreateDate());
						if(time > weiXinQrcodeInfoList.get(i).getExpireSeconds()){
							weiXinQrcodeInfoList.get(i).setExpireSecondsName("失效");
						}else{
							weiXinQrcodeInfoList.get(i).setExpireSecondsName("未失效");
						}
					}else{
						weiXinQrcodeInfoList.get(i).setTypeName(WeixinUtil.PERMANENT);
						weiXinQrcodeInfoList.get(i).setExpireSecondsName("未失效");
					}
				}
			}
			num = weiXinQrcodeInfoMapper.selectWeiXinQrcodeInfoNum(name);
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
		return new ListSlice(weiXinQrcodeInfoList, num);
	}

	@Override
	public void saveWeiXinQrcodeInfo(String name, String type,Integer expireSecondsDay) throws ApiException {
		LOG.info("save WeiXinQrcodeInfo");
		try {
			if (StringUtils.isEmpty(name)) {
				throw new ApiException("名称不能为空");
			}
			if (StringUtils.isEmpty(type)) {
				throw new ApiException("类型不能为空");
			}
			// 获取token
			AccessToken accessToken = accessTokenService.selectAccessToken(WeixinUtil.WEIXIN_APPID,
					WeixinUtil.WEIXIN_APPSECRET);
			WeiXinQrcodeInfo weiXinQrcodeInfo = new WeiXinQrcodeInfo();
			WeiXinQRCode weiXinQRCode = new WeiXinQRCode();
			weiXinQrcodeInfo.setName(name);
			weiXinQrcodeInfo.setCreateDate(DateUtil.getDate());
			weiXinQrcodeInfo.setSubscribeCount(new Long(0));
			weiXinQrcodeInfo.setType(type);
			weiXinQrcodeInfo.setDeleteFlag(false);
			if (accessToken != null) {
				// 临时二维码
				if (StringUtils.equals(type, WeixinUtil.QR_SCENE)) {
					if(expireSecondsDay == null){
						throw new ApiException("失效时间不能为空！");
					}
					if(expireSecondsDay <= 0){
						throw new ApiException("失效时间不能小于1天");
					}
					if(expireSecondsDay > 30){
						throw new ApiException("失效时间不能大于30天");
					}
					String maxId=String.valueOf(weiXinQrcodeInfoMapper.selectMaxSceheId());
					//失效时间 秒
					Long expireSecondSsecone = DateUtil.datToSecond(expireSecondsDay);
					String outputStr = WeixinUtil.TEMPORARY_QR_SCENE.replace("EXPIRE_SECONDS",String.valueOf(expireSecondSsecone)).replace("SCENE_ID", maxId);
					weiXinQRCode = WeixinUtil.establishQrcode(outputStr, accessToken.getToken(), type);
					weiXinQrcodeInfo.setQrCodeInfoId("qrscene_"+maxId);
					//失效时间
					weiXinQrcodeInfo.setExpireSeconds(weiXinQRCode.getExpireSeconds());
					weiXinQrcodeInfo.setUrl(weiXinQRCode.getUrl());
					weiXinQrcodeInfo.setQrCreateTime(weiXinQRCode.getCreateTime());
					weiXinQrcodeInfo.setExpireSeconds(weiXinQRCode.getExpireSeconds());
					weiXinQrcodeInfo.setTicket(weiXinQRCode.getTicket());
				}
				// 永久数字二维码
				else if (StringUtils.equals(type, WeixinUtil.QR_LIMIT_SCENE)) {
					/**
					 * 以后迭代业务逻辑
					 */
				}
				// 永久字符串二维码
				else if (StringUtils.equals(type, WeixinUtil.QR_LIMIT_STR_SCENE)) {
					String number = CodeGenerator.generatorOrderId();
					// 永久二维码传递参数
					String outputStr = WeixinUtil.PARAMETER_QR_LIMIT_STR_SCENE.replace("scene_number", number);
					weiXinQRCode = WeixinUtil.establishQrcode(outputStr, accessToken.getToken(), type);
					// 生成编号
					weiXinQrcodeInfo.setQrCodeInfoId("qrscene_" + number);
					weiXinQrcodeInfo.setUrl(weiXinQRCode.getUrl());
					weiXinQrcodeInfo.setQrCreateTime(weiXinQRCode.getCreateTime());
					weiXinQrcodeInfo.setExpireSeconds(weiXinQRCode.getExpireSeconds());
					weiXinQrcodeInfo.setTicket(weiXinQRCode.getTicket());
				}
				weiXinQrcodeInfoMapper.insert(weiXinQrcodeInfo);
			} else {
				throw new ApiException("获取token失败！");
			}
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
	}

	@Override
	public void updateSubscribeCountAddOne(String qrCodeInfoId) throws ApiException {
		LOG.info("updateSubscribeCountAddOne");
		try {
			WeiXinQrcodeInfo weiXinQrcodeInfo = weiXinQrcodeInfoMapper.selectWeiXinQrcodeInfoById(qrCodeInfoId);
			if (weiXinQrcodeInfo != null) {
				if (StringUtils.isEmpty(qrCodeInfoId)) {
					throw new ApiException("id不能为空");
				}
				weiXinQrcodeInfoMapper.updateSubscribeCountAddOne(qrCodeInfoId);
			} else {
				weiXinQrcodeInfoMapper.updateSubscribeCountAddOne(WeixinUtil.OTHER_QR_CODE_INFO);
			}
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
	}

	@Override
	public void deleteWeiXinQrcodeInfoById(String id) throws ApiException {
		try {
			if(StringUtils.isEmpty(id)){
				throw new ApiException("id不能为空");
			}
			WeiXinQrcodeInfo weiXinQrcodeInfo = new WeiXinQrcodeInfo();
			Example typeExp = new Example(WeiXinQrcodeInfo.class);
			typeExp.createCriteria().andEqualTo("qrCodeInfoId", id).andEqualTo("deleteFlag", 0);
			weiXinQrcodeInfo.setDeleteFlag(true);
			weiXinQrcodeInfoMapper.updateByExampleSelective(weiXinQrcodeInfo, typeExp);
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
	}

	@Override
	public Long countSubscribeCount() throws ApiException {
		Long count = new Long(0);
		try {
			count = weiXinQrcodeInfoMapper.countSubscribeCount();
		} catch (Exception e) {
			throw new ApiException(e.getMessage());
		}
		return count;
	}

}
