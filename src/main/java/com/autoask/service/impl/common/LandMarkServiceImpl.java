package com.autoask.service.impl.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.HttpClientUtil;
import com.autoask.entity.common.Address;
import com.autoask.entity.common.Landmark;
import com.autoask.service.common.LandMarkService;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp on 16-8-17.
 */
@Service("landMarkService")
public class LandMarkServiceImpl implements LandMarkService {
    private static Logger LOG = LoggerFactory.getLogger(LandMarkServiceImpl.class);

    @Override
    public Landmark getLandMark(Address address) throws ApiException {
        LOG.info("get address's landmark data.");

        Map<String, String> params = new HashMap<>();
        String addressString = address.toString();
        params.put("output", Constants.BaiduMap.INPUT_TYPE);
        params.put("ak", Constants.BaiduMap.KEY);
        params.put("address", String.valueOf(addressString));

        CloseableHttpResponse resp = null;
        try {
            List<Header> headerList = new ArrayList<>();
            headerList.add(new BasicHeader("accept", "*/*"));
            headerList.add(new BasicHeader("connection", "Keep-Alive"));
            headerList.add(new BasicHeader("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/51.0.2704.79 Chrome/51.0.2704.79 Safari/537.36"
            ));
            resp = HttpClientUtil.get(Constants.BaiduMap.MAP_URL, params, headerList, null);
            String jsonStr = IOUtils.toString(resp.getEntity().getContent());
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            JSONObject lankMarkResult = jsonObject.getJSONObject("result");
            JSONObject landMark = lankMarkResult.getJSONObject("location");
            Landmark landmarkObj = new Landmark();
            landmarkObj.setLatitude(landMark.getDouble("lat"));
            landmarkObj.setLongitude(landMark.getDouble("lng"));
            return landmarkObj;
        } catch (IOException e) {
            LOG.error(e.getMessage());
            return null;
        } finally {
            IOUtils.closeQuietly(resp);
        }
    }
}
