<%--
  Created by IntelliJ IDEA.
  User: eden_liu
  Date: 2016/5/27
  Time: 下午 05:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insertDefinition name="defaultTemplate">
    <tiles:putAttribute name="title">
        Index
    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        ${books}
    </tiles:putAttribute>
</tiles:insertDefinition>