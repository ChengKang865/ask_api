<!DOCTYPE html>
<html lang="en" ng-app="productApp">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="textml; charset=utf-8"/>

    <link rel="stylesheet" href="/autoask/static/lib/bower_components/uikit/css/uikit.min.css"/>
    <link rel="stylesheet" href="/autoask/static/lib/bower_components/uikit/css/uikit.almost-flat.min.css"/>

    <link rel="stylesheet" href="/autoask/static/lib/bower_components/uikit/css/components/slideshow.min.css"/>
    <link rel="stylesheet" href="/autoask/static/lib/bower_components/uikit/css/components/dotnav.min.css"/>
    <link rel="stylesheet" href="/autoask/static/lib/bower_components/uikit/css/components/slidenav.min.css"/>
    <link rel="stylesheet" href="/autoask/static/lib/bower_components/sweetalert/dist/sweetalert.css"/>

    <link rel="stylesheet" href="/autoask/static/css/public.css"/>
    <link rel="stylesheet" href="/autoask/static/css/index.css"/>

    <title></title>
</head>

<body id="pc">
<ng-include src="'/autoask/static/templates/pc/header.html'"></ng-include>

<!--begin: banner-->
<div class="uk-slidenav-position banner" data-uk-slideshow="{autoplay:true,animation:'swipe',height:'500px'}">
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
            <a href="/autoask/pc/${banner.productId}/"></a>
        </li>
    </#list>
    </ul>
</div>
<!--end: banner-->

<!--begin: main-->
<main class="main-container" ng-controller="HomeProductListController">
    <!--begin: 产品展示 -->
    <article class="product-display">
        <!--begin: 分类1-->
        <section class="product-classification">
            <h4><i class="icon icon-title-decorate"></i>NEW GEN</h4>
            <div class="product-list">
                <ul class="uk-list uk-grid uk-grid-collapse">
                    <li class="uk-width-1-3" data-product-detail-url="/autoask/pc/{{product.id}}/"
                        ng-repeat="product in newGenProdcutList" ng-click="onProductDetail($event)">
                        <div class="uk-margin-top uk-margin-bottom product-picture"><img src="{{product.img}}"/></div>
                        <div class="uk-container-center uk-width-6-10 uk-clearfix padding-large-left product-info">
                            <dl class="uk-float-left">
                                <dt>{{product.name}}</dt>
                                <dd>{{product.detail}}</dd>
                                <dd>{{product.price}}￥</dd>
                            </dl>
                            <a href="/autoask/pc/{{product.id}}/" class="uk-float-right btn-buy">立即购买</a>
                        </div>
                    </li>
                </ul>
            </div>
        </section>
        <!--end: 分类1-->

        <!--begin: 分类2-->
        <section class="product-classification">
            <h4><i class="icon icon-title-decorate"></i>CLASSIC</h4>
            <div class="product-list">
                <ul class="uk-list uk-grid uk-grid-collapse">
                    <li class="uk-width-1-3" data-product-detail-url="/autoask/pc/{{product.id}}/"
                        ng-repeat="product in classicProductList" ng-click="onProductDetail($event)">
                        <div class="uk-margin-top uk-margin-bottom product-picture"><img src="{{product.img}}"/></div>
                        <div class="uk-container-center uk-width-6-10 uk-clearfix padding-large-left product-info">
                            <dl class="uk-float-left">
                                <dt>{{product.name}}</dt>
                                <dd>{{product.detail}}</dd>
                                <dd>{{product.price}}￥</dd>
                            </dl>
                            <a href="/autoask/pc/{{product.id}}/" class="uk-float-right btn-buy">立即购买</a>
                        </div>
                    </li>
                </ul>
            </div>
        </section>
        <!--end: 分类2-->
    </article>
    <!--end: 产品展示 -->

    <article class="cooling-s-wrapper">
        <h4><i class="icon icon-title-decorate"></i>COOLING S +</h4>
        <section class="uk-text-center cooling-s">
            <p class="uk-container-center cooling-s-title">COOLING S +</p>
            <a href="" class="uk-button btn-buy">点击购买</a>
        </section>
    </article>

    <!--begin: auto autoask-->
    <article class="uk-text-center">
        <section class=" about-autoask-wrapper">
            <div class="uk-width-8-10 uk-container-center uk-text-left uk-grid">
                <div class="uk-width-1-2">
                    <h6 class="uk-h4">关于我们</h6>
                    <p class="content">
                        原谅我这一生不羁放纵爱自由 也会怕有一天会跌倒 背弃了理想 谁人都可以 哪会怕有一天只你共我 今天我 寒夜里看雪飘过 怀着冷却了的心窝漂远方 风雨里追赶 雾里分不清影踪 天空海阔你与我 可会变
                        (谁没在变) 原谅我这一生不羁放纵爱自由 也会怕有一天会跌倒 背弃了理想 谁人都可以 哪会怕有一天只你共我 仍然自由自我 永远高唱我歌 走遍千里 原谅我这一生不羁放纵爱自由 也会怕有一天会跌倒
                        背弃了理想 谁人都可以 哪会怕有一天只你共我 背弃了理想 谁人都可以 哪会怕有一天只你共我 原谅我这一生不羁放纵爱自由
                        也会怕有一天会跌倒 背弃了理想 谁人都可以
                    </p>
                    <a href="/html/pc/product_detail.html" class="uk-button uk-margin--top btn-about-me">了解AutoAsk</a>
                </div>
                <div class="uk-width-1-2">
                    <div class="uk-width-1 video-wrapper">
                        <object width="100%" height="100%" data=""></object>
                    </div>
                </div>
            </div>
        </section>
    </article>
    <!--end: auto autoask-->
</main>
<!--end: main-->

<ng-include src="'/autoask/static/templates/pc/footer.html'"></ng-include>

<script type="text/javascript" src="/autoask/static/lib/bower_components/jquery/dist/jquery.min.js"></script>
<script type="text/javascript" src="/autoask/static/lib/bower_components/angular/angular.min.js"></script>
<script type="text/javascript "
        src="/autoask/static/lib/bower_components/angular-cookies/angular-cookies.min.js "></script>

<script type="text/javascript" src="/autoask/static/lib/bower_components/uikit/js/uikit.min.js"></script>
<script type="text/javascript " src="/autoask/static/lib/bower_components/uikit/js/components/slideshow.min.js "></script>
<script type="text/javascript" src="/autoask/static/lib/bower_components/sweetalert/dist/sweetalert.min.js"></script>

<script type="text/javascript" src="/autoask/static/js/lib.js"></script>

<script type="text/javascript" src="/autoask/static/js/app.js"></script>
</body>

</html>