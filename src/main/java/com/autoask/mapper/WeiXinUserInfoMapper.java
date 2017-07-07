package com.autoask.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.autoask.entity.mysql.WeiXinUserInfo;
import com.autoask.mapper.base.MyMapper;

public interface WeiXinUserInfoMapper extends MyMapper<WeiXinUserInfo>{
	List<WeiXinUserInfo> selectWeiXinUserInfoList(@Param("nickName") String nickName,@Param("deleteFlag") Boolean deleteFlag,@Param("start") Integer start, @Param("limit") Integer limit);
	
	WeiXinUserInfo selectWeiXinUserInfoById(@Param("openId") String openId);
	
	Long selectWeiXinUserInfoNum(@Param("nickName") String nickName);
	
	int cancelAttention(@Param("openId") String openId);
	
	int subscribe(@Param("openId") String openId);
}
