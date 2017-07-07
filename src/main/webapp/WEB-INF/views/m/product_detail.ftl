<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en" ng-app="productApp">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta http-equiv="Content-Type" content="textml; charset=utf-8"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <link rel="stylesheet" href="${basePath}/static/lib/bower_components/uikit/css/uikit.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/lib/bower_components/uikit/css/uikit.almost-flat.min.css"/>

    <link rel="stylesheet" href="${basePath}/static/lib/bower_components/uikit/css/components/slideshow.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/lib/bower_components/uikit/css/components/dotnav.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/lib/bower_components/uikit/css/components/slidenav.min.css"/>
    <link rel="stylesheet" href="${basePath}/static/lib/bower_components/sweetalert/dist/sweetalert.css"/>


    <link rel="stylesheet" href="${basePath}/static/css/public.css"/>
    <link rel="stylesheet" href="${basePath}/static/css/product_detail.css"/>

    <title></title>
</head>

<body>
<ng-include src="'${basePath}/static/templates/header.html'"></ng-include>

<main class="main-container product-detail" ng-controller="ProductDetailController">
    <!--begin: product picture -->
    <aside class="uk-slidenav-position banner" data-uk-slideshow="{autoplay:true,animation:'swipe'}">
        <ul class="uk-slideshow">
        <#list picUrlList as picUrl>
            <li>
                <img src="${picUrl}" width="" height="" alt="">
            </li>
        </#list>
        </ul>

        <ul class="uk-dotnav uk-dotnav-contrast uk-position-bottom uk-text-center">
            <li data-uk-slideshow-item="0">
                <a href=""></a>
            </li>
            <li data-uk-slideshow-item="1">
                <a href=""></a>
            </li>
            <li data-uk-slideshow-item="2">
                <a href=""></a>
            </li>
        </ul>
    </aside>
    <!--end: product picture -->

    <!--begin: product-->
    <article class="uk-margin-bottom product-info" ng-init="productDetail.productId= ' ${productId}'">
        <input type="text" ng-model="goodsMap" ng-init="goodsMap = ${goodsMapStr}" style="display: none;"/>

        <!--begin: product detail info-->
        <div class="info">
            <dl class="padding-top">
                <dt ng-init="goods.name = 'aaaa'">{{goods.name}}</dt>
                <dd ng-init="goods.detailName = 'bbb'">{{goods.detailName}}</dd>
                <dd class="price uk-margin-top" ng-init="goods.onLinePrice =
                    '${onLinePrice}'" ng-if="goods.onLinePrice !== 0">产品价格<strong class="padding-left padding-right">{{goods.onLinePrice}}元</strong>
                </dd>
                <dd class="price uk-margin-top" ng-init="goods.offLinePrice
                    = '${offLinePrice}'" ng-if="goods.offLinePrice !== 0">
                    产品价格<strong class="padding-left padding-right">{{goods.offLinePrice}}元</strong>
                </dd>
            </dl>
        </div>
        <!--end: product detail info-->

        <!--begin: 购物操作区-->
        <div class="product-operation-wrapper">
            <form class="uk-form">
            <#list metaMap?keys as key>
                <fieldset>
                    <label>${key}</label>
                    <ul class="uk-list button-group goods-labels" data-key="${key}">
                        <#list metaMap[key] as value>
                            <li>
                                <button class="uk-button goods-label" ng-click="choiceLabel($event)">${value}</button>
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
                        <input type="text" readonly="readonly" class="uk-width-1-3" ng-model="productDetail.number"/>
                        <button type="button" class="uk-button uk-width-1-3"
                                ng-click="productDetail.number=productDetail.number+1"><i class="uk-icon-plus"></i>
                        </button>
                    </div>
                </fieldset>
            </form>
        </div>
        <!--end: 购物操作区-->
    </article>
    <!--end: product-->

    <!--begin: comment list-->
    <article class="uk-margin-bottom user-comment-list-wrapper">
        <h4 class="padding-top"><i class="icon icon-title-decorate"></i>用户评价</h4>
        <ul class="uk-list comment-list">
            <li class="comment padding-top padding-bottom" ng-repeat="comment in commentList"
                ng-repeat="comement in commentList">
                <dl>
                    <dt><i class="icon icon-avatar"></i>{{comment.userPhone}}</dt>
                    <dd class="uk-width-9-10">
                        <p class="padding-top">{{comment.comment}}</p>
                    </dd>
                    <dd class="additional-info">
                        <span class="uk-margin-right">{{comment.time}}</span>
                        <span class="uk-margin-right">{{comment.productName}}</span>
                    </dd>
                </dl>
            </li>
        </ul>
    </article>
    <!--end: comment list-->

    <article class="uk-margin-bottom">
        <h4 class="padding-top"><i class="icon icon-title-decorate"></i>推荐产品</h4>
        <ul class="uk-list pictures">
            <li class="padding-bottom padding-right">
                <a href=""><img src="http://www.getuikit.net/docs/images/placeholder_800x400_1.jpg" alt=""></a>
            </li>
            <li class="padding-bottom padding-right">
                <a href=""><img src="http://www.getuikit.net/docs/images/placeholder_800x400_1.jpg" alt=""></a>
            </li>
        </ul>
    </article>

    <footer class="uk-grid uk-grid-collapse">
        <button type="button" class="uk-button uk-width-1-2 btn-add-shopping-cart"
                data-shopping-cart-url="/html/mobile/shopping_cart.html"
                ng-click="onShoppingCart($event)"><i class="icon icon-shopping-cart"></i>加入购物车
        </button>
        <button type="button" class="uk-button uk-width-1-2 btn-buy-now"
                data-shopping-cart-url="/html/mobile/shopping_cart.html"
                ng-click="onShoppingCart($event, true)">立即购买
        </button>
    </footer>
</main>
</body>

<script type="text/javascript" src="${basePath}/static/lib/bower_components/jquery/dist/jquery.min.js"></script>
<script type="text/javascript" src="${basePath}/static/lib/bower_components/angular/angular.min.js"></script>
<script type="text/javascript"
        src="${basePath}/static/lib/bower_components/angular-cookies/angular-cookies.min.js"></script>

<script type="text/javascript" src="${basePath}/static/lib/bower_components/uikit/js/uikit.min.js"></script>
<script type="text/javascript"
        src="${basePath}/static/lib/bower_components/uikit/js/components/slideshow.min.js"></script>

<script type="text/javascript"
        src="${basePath}/static/lib/bower_components/sweetalert/dist/sweetalert.min.js"></script>

<script type="text/javascript" src="${basePath}/static/js/app.js"></script>

</html>
