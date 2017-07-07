<!DOCTYPE html>
<html lang="en" ng-app="productApp">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="textml; charset=utf-8"/>

    <link rel="stylesheet" href="/autoask/static/lib/bower_components/uikit/css/uikit.min.css"/>
    <link rel="stylesheet" href="/autoask/static/lib/bower_components/uikit/css/uikit.almost-flat.min.css"/>
    <link rel="stylesheet" href="/autoask/static/lib/bower_components/sweetalert/dist/sweetalert.css"/>

    <link rel="stylesheet" href="/autoask/static/css/public.css"/>
    <link rel="stylesheet" href="/autoask/static/css/product_detail.css"/>

    <title></title>
</head>

<body id="pc">
<ng-include src="'/autoask/static/templates/pc/header.html'"></ng-include>

<input type="text" ng-model="goodsMap"
       ng-init="goodsMap = {'速霸(ow/30 1L)':{'goodsSnapshotId':'01e831b873484a74bced4bec9cf80464','goodsNameEn':'subai(0w/30 1L)','goodsId':'5a85be46dbf140afa76168005aee95bc','goodsPic':'http://obfd49wzy.bkt.clouddn.com/4687529e02214102b07e5d638c86aff3.jpg','goodsLabel':'粘度等级=0w/30##容量=1L##','goodsName':'速霸(ow/30 1L)','onLinePrice':1.00,'offLinePrice':1.00},'速霸(5w/50 2.4L)':{'goodsSnapshotId':'af553a72695c424386cf61712727433f','goodsNameEn':'suba(5w/50 2.4L)','goodsId':'940774e2ea64482d80c20ca1fcad4570','goodsPic':'http://obfd49wzy.bkt.clouddn.com/4687529e02214102b07e5d638c86aff3.jpg','goodsLabel':'粘度等级=5w/50##容量=2.4L##','goodsName':'速霸(5w/50 2.4L)','onLinePrice':1.00,'offLinePrice':1.00},'速霸(0w/40 2L)':{'goodsSnapshotId':'baf63c862e13430ca182dbdb1d9c7c5c','goodsNameEn':'subai(0w/40 2L)','goodsId':'ed4f13ff7ac34ad2b179440298b60d9b','goodsPic':'http://obfd49wzy.bkt.clouddn.com/4687529e02214102b07e5d638c86aff3.jpg','goodsLabel':'粘度等级=0w/40##容量=2L##','goodsName':'速霸(0w/40 2L)','onLinePrice':1.00,'offLinePrice':1.00},'速霸(0w/30 2L)':{'goodsSnapshotId':'ff2aca139f484a6aa0a7166773509404','goodsNameEn':'subai(0w/30 2L)','goodsId':'b9e0b58889144502a7349b30799065df','goodsPic':'http://obfd49wzy.bkt.clouddn.com/4687529e02214102b07e5d638c86aff3.jpg','goodsLabel':'粘度等级=0w/30##容量=2L##','goodsName':'速霸(0w/30 2L)','onLinePrice':1.00,'offLinePrice':1.00},'速霸(0w/40 1L)':{'goodsSnapshotId':'5373bcdb7e374a2284bf755aab543276','goodsNameEn':'suba(0w/40 1L)','goodsId':'45b1d291971c4c6395abab74b7a6f17f','goodsPic':'http://obfd49wzy.bkt.clouddn.com/4687529e02214102b07e5d638c86aff3.jpg','goodsLabel':'粘度等级=0w/40##容量=1L##','goodsName':'速霸(0w/40 1L)','onLinePrice':1.00,'offLinePrice':1.00},'速霸(0w/30 2.4L)':{'goodsSnapshotId':'8b1653c07a8b4c8fba8578b76bc2208d','goodsNameEn':'subai(0w/30 2.4L)','goodsId':'5d85456a394a4d3cb565b84837124b35','goodsPic':'http://obfd49wzy.bkt.clouddn.com/4687529e02214102b07e5d638c86aff3.jpg','goodsLabel':'粘度等级=0w/30##容量=2.4L##','goodsName':'速霸(0w/30 2.4L)','onLinePrice':1.00,'offLinePrice':1.00}}"
       style="display: none;"/>

<main class="main-container product-detail" ng-controller="ProductDetailController">
    <!--begin: product-->
    <article class="uk-margin-large-top uk-margin-bottom padding-large-bottom product-info"
             ng-init="productDetail.productId= ' ${productId}' ">
        <input type=" text
" ng-model="goodsMap" ng-init="goodsMap = ${goodsMapStr}" style="display: none;"/>
        <div class="uk-width-8-10 uk-container-center uk-grid product-picture">
            <div class="uk-width-1-2 pictures uk-cloearfix">
                <div class="uk-float-left thumbnails">
                    <ul class="uk-list">
                        <li ng-repeat="picture in productDetail.thumbnails">
                            <a href=""><img src="{{picture.img}}" alt=""/></a>
                        </li>
                    </ul>
                </div>
                <div class="uk-float-left big-picture">
                    <img src="http://www.getuikit.net/docs/images/placeholder_800x400_1.jpg" alt=""/>
                </div>
            </div>
            <div class="uk-width-1-2 product-info">
                <h4 class="uk-h2">${productName}</h4>
                <h6 class="uk-h4">${productNameEn}</h6>

                <!--begin: 购物操作区-->
                <div class="product-operation-wrapper">
                    <form class="uk-form">
                    <#list metaMap?keys as key>
                        <fieldset>
                            <label>${key}</label>
                            <ul class="uk-list button-group goods-labels" data-key="${key}">
                                <#list metaMap[key] as value>
                                    <li>
                                        <button class="uk-button goods-label"
                                                ng-click="choiceLabel($event)">${value}</button>
                                    </li>
                                </#list>
                            </ul>
                        </fieldset>
                    </#list>

                        <fieldset>
                            <label>购买方式</label>
                            <ul class="uk-list button-group">
                                <li>
                                    <button class="uk-button" ng-click="choiceBuyWay($event, 'delivered')">线上购</button>
                                </li>
                                <li>
                                    <button class="uk-button" ng-click="choiceBuyWay($event, 'pickup')">线下购</button>
                                </li>
                            </ul>
                        </fieldset>

                        <fieldset>
                            <label>产品数量</label>
                            <div class="uk-cloearfix spinner uk-grid uk-grid-collapse">
                                <button type="button" class="uk-button uk-width-1-3"
                                        ng-click="productDetail.number=productDetail.number-1"
                                        ng-disabled="productDetail.number===0"><i class="uk-icon-minus"></i></button>
                                <input type="text" readonly="readonly" class="uk-width-1-3"
                                       ng-model="productDetail.number"/>
                                <button type="button" class="uk-button uk-width-1-3"
                                        ng-click="productDetail.number=productDetail.number+1"><i
                                        class="uk-icon-plus"></i>
                                </button>
                            </div>
                        </fieldset>

                        <fieldset>
                            <div class="padding-top button-group buy-button-group">
                                <button type="button" class="uk-button uk-margin-right"
                                        data-shopping-cart-url="/html/pc/shopping_cart.html"
                                        ng-click="onShoppingCart($event, true)">立即购买
                                </button>
                                <button type="button" class="uk-button"
                                        data-shopping-cart-url="/html/pc/shopping_cart.html"
                                        ng-click="onShoppingCart($event)">加入购物车
                                </button>
                            </div>
                        </fieldset>
                    </form>
                </div>
                <!--end: 购物操作区-->
            </div>
        </div>
    </article>
    <!--end: product-->

    <!-- begin: 推荐产品 -->
    <article>
        <h4 class="padding-top padding-bottom"><i class="icon icon-title-decorate"></i>推荐产品</h4>
        <ul class="uk-list uk-grid uk-grid-collapse recommendeded-products">
        <#list recommendProductList as product>
            <li class="uk-width-1-3 uk-text-center padding-top padding-bottom">
                <div class="uk-text-left padding-large-left product-info">
                    <div class="product-picture"><img src="${product.logoUrl}"/></div>
                    <dl class="uk-margin-top">
                        <dt>${product.name}</dt>
                        <dd>${product.description}</dd>
                        <dd>￥ ${product.lowPrice}</dd>
                    </dl>
                    <a href="/autoask/pc/${product.productId}/" class="uk-button btn-buy">立即购买</a>
                </div>
            </li>
        </#list>

        </ul>
    </article>
    <!--end: 推荐产品 -->
</main>

<ng-include src="'/autoask/static/templates/pc/footer.html '"></ng-include>

<script type="text/javascript" src="/autoask/static/lib/bower_components/jquery/dist/jquery.min.js"></script>
<script type="text/javascript" src="/autoask/static/lib/bower_components/angular/angular.min.js"></script>
<script type="text/javascript"
        src="/autoask/static/lib/bower_components/angular-cookies/angular-cookies.min.js"></script>

<script type="text/javascript" src="/autoask/static/lib/bower_components/uikit/js/uikit.min.js"></script>
<script type="text/javascript" src="/autoask/static/lib/bower_components/sweetalert/dist/sweetalert.min.js"></script>

<script type="text/javascript" src="/autoask/static/js/app.js"></script>
</body>

</html>