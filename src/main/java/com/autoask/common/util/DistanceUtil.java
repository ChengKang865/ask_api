package com.autoask.common.util;


import com.autoask.entity.common.Landmark;

/**
 * Created by Administrator on 2015/9/23.
 */
public class DistanceUtil {

    public static final double EARTH_RADIUS = 6378137;//赤道半径(单位m)

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double getDistance(Landmark landmark1, Landmark landmark2) {
        double radLat1 = rad(landmark1.getLatitude());
        double radLat2 = rad(landmark2.getLatitude());
        double a = radLat1 - radLat2;
        double b = rad(landmark1.getLongitude()) - rad(landmark2.getLongitude());
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        //s = Math.round(s * 10000) / 10000;
        return s;
    }
}
