package com.autoask.entity.mongo.config;

import com.autoask.entity.mongo.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author hyy
 * @create 2016-10-28 22:58
 */
@Document
public class GlobalConfig extends BaseEntity {

    /**
     * 广告费 分成半径范围
     */
    private Long adFeeCircleDistance;

    public Long getAdFeeCircleDistance() {
        return adFeeCircleDistance;
    }

    public void setAdFeeCircleDistance(Long adFeeCircleDistance) {
        this.adFeeCircleDistance = adFeeCircleDistance;
    }
}
