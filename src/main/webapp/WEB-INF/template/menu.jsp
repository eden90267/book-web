<%--
  Created by IntelliJ IDEA.
  User: eden90267
  Date: 2016/5/28
  Time: 上午10:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div class="menu">
    Menu
    <ul>
        <li>
            <spring:url value="/book/index" var="indexUrl" htmlEscape="true"/>
            <a href="${indexUrl}">index</a>
        </li>
    </ul>
</div>