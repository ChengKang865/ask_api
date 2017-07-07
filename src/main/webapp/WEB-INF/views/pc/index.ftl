<!DOCTYPE html>
<html lang="en" ng-app="productApp">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="textml; charset=utf-8"/>

    <link rel="stylesheet" href="/autoask/static/lib/bower_components/uikit/css/uikit.min.css"/>
    <link rel="stylesheet" href="/autoask/static/lib/bower_components/uikit/css/uikit.almost-flat.min.css"/>

    <link rel="stylesheet" href="/autoask/static/css/public.css"/>
    <link rel="stylesheet" href="/autoask/static/css/index.css"/>

    <title></title>
</head>

<body>
<ng-include src="'/autoask/static/templates/pc/header.html'"></ng-include>


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
            <a href="/autoask/pc/${banner.productId}/"></a>
        </li>
    </#list>
    </ul>
</div>


<ng-include src="'/autoask/static/templates/pc/footer.html'"></ng-include>

<script type="text/javascript" src="/autoask/static/lib/bower_components/jquery/dist/jquery.min.js"></script>
<script type="text/javascript" src="/autoask/static/lib/bower_components/angular/angular.min.js"></script>
<script type="text/javascript "
        src="/autoask/static/lib/bower_components/angular-cookies/angular-cookies.min.js "></script>

<script type="text/javascript" src="/autoask/static/lib/bower_components/uikit/js/uikit.min.js"></script>

<script type="text/javascript" src="/autoask/static/js/app.js"></script>
</body>

</html>