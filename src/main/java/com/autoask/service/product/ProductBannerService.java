package com.autoask.service.product;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.product.ProductBanner;
import com.autoask.service.BaseMongoService;

import java.util.List;

/**
 * Created by hp on 16-9-24.
 */
public interface ProductBannerService extends BaseMongoService<ProductBanner> {

    /**
     * 创建一个banner
     *
     * @param productBanner
     * @throws ApiException
     */
    void createProductBanner(ProductBanner productBanner) throws ApiException;

    /**
     * 获取productBanner
     *
     * @param productBannerId
     * @return
     * @throws ApiException
     */
    ProductBanner getProductBannerById(String productBannerId) throws ApiException;

    /**
     * 获取banner 列表
     *
     * @return
     * @throws ApiException
     */
    List<ProductBanner> getProductBannerList() throws ApiException;

    /**
     * 上线banner
     *
     * @param productBannerId
     * @throws ApiException
     */
    void onlineProductBanner(String productBannerId) throws ApiException;

    /**
     * 下线banner
     *
     * @param productBannerId
     * @throws ApiException
     */
    void offlineProductBanner(String productBannerId) throws ApiException;

    /**
     * 删除banner
     *
     * @param productBannerId
     * @throws ApiException
     */
    void deleteProductBanner(String productBannerId) throws ApiException;

    /**
     * 查询banner的数量
     *
     * @return
     */
    Integer getProductBannerCount();
}
