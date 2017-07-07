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

    <link rel="stylesheet" href="/autoask/static/lib/bower_components/uikit/css/uikit.min.css"/>
    <link rel="stylesheet" href="/autoask/static/lib/bower_components/uikit/css/uikit.almost-flat.min.css"/>

    <link rel="stylesheet" href="/autoask/static/lib/bower_components/uikit/css/components/slideshow.min.css"/>
    <link rel="stylesheet" href="/autoask/static/lib/bower_components/uikit/css/components/dotnav.min.css"/>
    <link rel="stylesheet" href="/autoask/static/lib/bower_components/uikit/css/components/slidenav.min.css"/>

    <link rel="stylesheet" href="/autoask/static/lib/bower_components/sweetalert/dist/sweetalert.css" />

    <link rel="stylesheet" href="/autoask/static/css/public.css"/>
    <link rel="stylesheet" href="/autoask/static/css/index.css"/>

    <title></title>
</head>
<body>
<ng-include src="'/autoask/static/templates/header.html'"></ng-include>
<!--begin: banner-->
<div class="uk-slidenav-position banner" data-uk-slideshow="{autoplay:true,animation:'swipe'}">
    <ul class="uk-slideshow">
    <#list bannerList as banner>
        <li>
            <img src="${banner.picUrl!}" width="" height="" alt="">
        </li>
    </#list>
    </ul>

    <ul class="uk-dotnav uk-dotnav-contrast uk-position-bottom uk-text-center">
    <#list bannerList as banner>
        <li data-uk-slideshow-item="0">
            <a href="/autoask/m/${banner.productId}/"></a>
        </li>
    </#list>
    </ul>
</div>
<!--end: banner-->

<!--begin: main-->
<main class="main-container" ng-controller="HomeProductListController">
<#--<input type="text" style="display:none" ng-model="productCategoryList" ng-init="productCategoryList = ${productCategoryList}" />-->
    <!--begin: 产品展示 --->
    <article class="product-display">
        <!--begin: 分类1--->
        <section class="product-classification">
            <h4>NEW GEN</h4>
            <div class="product-list">
                <ul class="uk-list uk-grid uk-grid-collapse">
                <#list newGenList as newGenProduct>
                    <li class="uk-width-1-2" data-product-detail-url="/html/mobile/product_detail.html">
                        <div class="product-picture">
                            <img src="${newGenProduct.picUrl}"/>
                        </div>
                        <div class="product-info">
                            <dl>
                                <dt>${newGenProduct.product.name}</dt>
                                <dd>${newGenProduct.product.description}</dd>
                                <dd>${newGenProduct.minPrice}￥</dd>
                            </dl>
                            <a href="/autoask/m/${newGenProduct.productId}/" class="btn-buy">立即购买</a>
                        </div>
                    </li>
                </#list>
                </ul>
            </div>
        </section>
        <!--end: 分类1--->

        <!--begin: 分类2--->
        <section class="product-classification">
            <h4>CLASSIC</h4>
            <div class="product-list">
                <ul class="uk-list uk-grid uk-grid-collapse">
                <#list classicList as classicProduct>
                    <li class="uk-width-1-2" data-product-detail-url="/html/mobile/product_detail.html">
                        <div class="product-picture">
                            <img src="${classicProduct.picUrl}"/>
                        </div>
                        <div class="product-info">
                            <dl>
                                <dt>${classicProduct.product.name}</dt>
                                <dd>${classicProduct.product.description}</dd>
                                <dd>${classicProduct.minPrice}￥</dd>
                            </dl>
                            <a href="/autoask/m/${classicProduct.productId}/" class="btn-buy">立即购买</a>
                        </div>
                    </li>
                </#list>
                </ul>
            </div>
        </section>
        <!--end: 分类2--->
    </article>
    <!--end: 产品展示 --->
</main>
<!--end: main-->

<ng-include src="'/autoask/static/templates/footer.html'"></ng-include>
<script type="text/javascript" src="/autoask/static/lib/bower_components/jquery/dist/jquery.min.js"></script>
<script type="text/javascript" src="/autoask/static/lib/bower_components/angular/angular.min.js"></script>
<script type="text/javascript "
        src="/autoask/static/lib/bower_components/angular-cookies/angular-cookies.min.js "></script>

<script type="text/javascript" src="/autoask/static/lib/bower_components/uikit/js/uikit.min.js"></script>
<script type="text/javascript" src="/autoask/static/lib/bower_components/uikit/js/components/slideshow.min.js"></script>

<script type="text/javascript" src="/autoask/static/lib/bower_components/sweetalert/dist/sweetalert.min.js"></script>

<script type="text/javascript" src="/autoask/static/js/app.js"></script>
</body>

</html>