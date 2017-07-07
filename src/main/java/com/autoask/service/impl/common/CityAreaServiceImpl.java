package com.autoask.service.impl.common;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mysql.CityArea;
import com.autoask.mapper.CityAreaMapper;
import com.autoask.service.common.CityAreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by hp on 16-8-8.
 */
@Service("cityArea")
public class CityAreaServiceImpl implements CityAreaService {
    private Logger LOG = LoggerFactory.getLogger(CityAreaService.class);

    @Autowired
    private CityAreaMapper cityAreaMapper;

    @Override
    public List<CityArea> getCityAreaByParentId(Integer parentId) throws ApiException {
        LOG.info("get city by parent'd id");

        Example example = new Example(CityArea.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", parentId);

        try {
            List<CityArea> cityAreas = cityAreaMapper.selectByExample(example);
            return cityAreas;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    @Override
    public List<String> getStreets(String province, String city, String region) {
        LOG.info("get street");

        return cityAreaMapper.getStreets(province, city, region);
    }
}
