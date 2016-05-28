<%--
  Created by IntelliJ IDEA.
  User: eden90267
  Date: 2016/5/28
  Time: 上午10:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><tiles:insertAttribute name="title"/></title>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.aspnetcdn.com/ajax/jquery.validate/1.15.0/jquery.validate.js"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
    <script type="text/javascript" src="<s:url value="/resources/js"/>/jquery-ui-timepicker-addon.js"></script>
    <script type="text/javascript" src="<s:url value="/resources/js"/>/jquery-ui-sliderAccess.js"></script>
    <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/themes/hot-sneaks/jquery-ui.css" rel="stylesheet">
    <link href="<s:url value="/resources/css"/>/jquery-ui-timepicker-addon.css" rel="stylesheet"/>
    <link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.6.0/pure-min.css">
    <style type="text/css">
        body {
            margin: 0px;
            padding: 0px;
            height: 100%;
            overflow: hidden;
            font-family: 'trebuchet MS', 'Lucida sans', Arial;
            font-size: 14px;
            color: #444;
        }

        .page {
            min-height: 100%;
            position: relative;
        }

        .header {
            padding-top: 10px;
            padding-bottom: 10px;
            width: 100%;
            text-align: center;
            border: 1px #ccc solid;
            border-radius: 10px;
        }

        .content {
            padding: 10px;
            padding-bottom: 20px; /* Height of the footer element */
            overflow: hidden;
        }

        .menu {
            padding: 10px 10px 0px 10px;
            width: 15%;
            float: left;
            border: 1px #ccc solid;
            border-radius: 10px;
        }

        .body {
            margin: 82px 10px 0px 17%;
            width: 81%;
            padding: 5px;
            position: absolute;
            top: 0px;
            border: 1px #ccc solid;
            border-radius: 10px;
        }

        .footer {
            clear: both;
            position: absolute;
            bottom: 0;
            left: 0;
            text-align: center;
            width: 100%;
            padding-top: 3px;
            height: 17px;
            border: 1px #ccc solid;
            border-radius: 10px;
        }

        .error {
            color: red;
        }
    </style>
</head>
<body>
<div class="page">
    <tiles:insertAttribute name="header"/>
    <div class="content">
        <tiles:insertAttribute name="menu"/>
        <div class="body">
            <tiles:insertAttribute name="body"/>
        </div>
    </div>
    <tiles:insertAttribute name="footer"/>
</div>
</body>
</html>