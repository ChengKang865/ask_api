package com.autoask.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.autoask.entity.mysql.WeiXinQrcodeInfo;
import com.autoask.mapper.base.MyMapper;

public interface WeiXinQrcodeInfoMapper extends MyMapper<WeiXinQrcodeInfo>{
	List<WeiXinQrcodeInfo> selectWeiXinQrcodeInfoList(@Param("name") String name,@Param("deleteFlag") Boolean deleteFlag,@Param("start") Integer start, @Param("limit") Integer limit);
	
	WeiXinQrcodeInfo selectWeiXinQrcodeInfoById(@Param("qrCodeInfoId") String qrCodeInfoId);
	
	int updateSubscribeCountAddOne(@Param("qrCodeInfoId")String qrCodeInfoId);
	
	Long countSubscribeCount();
	
	Long selectMaxSceheId();
	
	Long selectWeiXinQrcodeInfoNum(@Param("name") String name);
	
}
