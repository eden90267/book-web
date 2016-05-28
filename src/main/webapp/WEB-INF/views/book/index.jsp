<%--
  Created by IntelliJ IDEA.
  User: eden_liu
  Date: 2016/5/27
  Time: 下午 05:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:insertDefinition name="defaultTemplate">
    <tiles:putAttribute name="title">
        Index
    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <script language="JavaScript">
            function deleteBook(url) {
                var confirmed = confirm('You sure you want to delete it?');

                if (confirmed) {
                    var request = new XMLHttpRequest();
                    request.open('DELETE', url, true);
                    request.onreadystatechange = function () {
                        window.location.reload(true);
                    };
                    request.send(null);
                }
            }
        </script>
        <h2>book list</h2>
        <div style="width: 100%; text-align: right; margin-bottom: 5px;">
            <s:url value="/book/create" var="createUrl"/>
            <a href="${createUrl}">Create</a>
        </div>
        <table width="100%" class="pure-table">
            <thead>
            <tr>
                <th align="center">Action</th>
                <th>BookName</th>
                <th>BookPrice</th>
                <th align="center">BookImage</th>
                <th>CreateTime</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="book" items="${books}">
                <tr>
                    <td align="center">
                        <s:url value="/book/edit/{id}" var="editUrl">
                            <s:param name="id" value="${book.id}"/>
                        </s:url>
                        <a href="${editUrl}">Edit</a>

                        <s:url value="/book/{id}" var="deleteUrl">
                            <s:param name="id" value="${book.id}"/>
                        </s:url>
                        <a href="javascript:deleteBook('<c:out value="${deleteUrl}"/>')">Delete</a>
                    </td>
                    <td>${book.bookName}</td>
                    <td align="right">
                        <fmt:setLocale value="zh_TW"/>
                        <fmt:formatNumber value="${book.bookPrice}" type="currency" maxFractionDigits="0"/>
                    </td>
                    <td align="center">
                        <c:if test="${book.bookImage != null}">
                            <img src="${bookS3Link}${book.bookImage}"
                                 width="48"
                                 border="0"
                                 align="middle"
                                 onerror="this.src='<s:url value="/resources/images"/>/404.png';"/>
                        </c:if>
                    </td>
                    <td align="center">
                        <fmt:formatDate value="${book.createTime}" pattern="yyyy/MM/dd HH:mm:ss"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </tiles:putAttribute>
</tiles:insertDefinition>