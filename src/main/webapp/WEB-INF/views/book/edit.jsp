<%--
  Created by IntelliJ IDEA.
  User: eden90267
  Date: 2016/5/28
  Time: 下午5:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<tiles:insertDefinition name="defaultTemplate">
    <tiles:putAttribute name="title">
        Edit
    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <script language="JavaScript">
            function deleteImage(url) {
                var confirmed = confirm('You sure you want to delete image?');

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
        <h2>Edit Book</h2>
        <sf:form id="editForm" method="post" commandName="book" modelattribute="book" enctype="multipart/form-data">
            <table cellspacing="0" class="pure-table pure-table-bordered">
                <tbody>
                <tr>
                    <th><sf:label path="bookName">Book Name:</sf:label></th>
                    <td>
                        <sf:input id="bookName" path="bookName" size="15" maxlength="255"/>
                        <br/>
                        <sf:errors path="bookName" cssClass="error"/>
                    </td>
                </tr>
                <tr>
                    <th><sf:label path="bookPrice">Book Price:</sf:label></th>
                    <td>
                        <sf:input id="bookPrice" path="bookPrice" size="15"/>
                        <br/>
                        <sf:errors path="bookPrice" cssClass="error"/>
                    </td>
                </tr>
                <tr>
                    <th><label for="bookImage">Book Image:</label></th>
                    <td>
                        <s:eval expression="@environment.getProperty('book.s3.url')" var="bookS3Url"/>
                        <s:eval expression="@environment.getProperty('book.s3.bucket')" var="bookS3Bucket"/>
                        <c:if test="${book.bookImage != null}">
                            <a href="${bookS3Url}/${bookS3Bucket}/${book.bookImage}" target="_blank">
                                <img src="${bookS3Url}/${bookS3Bucket}/${book.bookImage}"
                                     width="256px"
                                     border="0"
                                     align="middle"
                                     onerror="this.src='<s:url value="/resources/images"/>/404.png';"/>
                            </a>
                            <s:url value="/book/deleteImage/{id}" var="deleteImageUrl">
                                <s:param name="id" value="${book.id}"/>
                            </s:url>
                            <a href="javascript:deleteImage('<c:out value="${deleteImageUrl}"/>')">
                                <img src="<s:url value="/resources/images"/>/delete_pic.png"
                                     border="0" align="top"/>
                            </a>
                            <br/>
                            <small>Not the original file size, click on the picture to open another tab.</small>
                            <sf:hidden path="bookImage"/>
                        </c:if>
                        <c:if test="${book.bookImage == null}">
                            <input id="image" name="image" type="file"/>
                            <br/>
                            <sf:errors path="bookImage" cssClass="error"/>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <th><sf:label path="createTime">Create Time:</sf:label></th>
                    <td>
                        <sf:input id="createTime" path="createTime" size="19" maxlength="19"/>
                        <script language="JavaScript">
                            $(document).ready(function () {
                                var opt = {
                                    dateFormat: 'yy/mm/dd',
                                    showSecond: true,
                                    timeFormat: 'HH:mm:ss'
                                };
                                $('#createTime').datetimepicker(opt);
                            });
                        </script>
                        <br/>
                        <sf:errors path="createTime" cssClass="error"/>
                    </td>
                </tr>
                <tr>
                    <th></th>
                    <td>
                        <input name="commit" type="submit" value="Update"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </sf:form>
        <script>
            $(document).ready(function () {
                $('ul.navbar-nav li').removeClass("active");
                $('ul.navbar-nav li:nth-child(4)').addClass("active");
                $("#bookPrice").keydown(function (e) {
                    if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
                            (e.keyCode == 65 && ( e.ctrlKey === true || e.metaKey === true ) ) ||
                            (e.keyCode >= 35 && e.keyCode <= 40)) {
                        return;
                    }
                    if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
                        e.preventDefault();
                    }
                });
                $.validator.addMethod("dateTime", function (value, element) {
                    var stamp = value.split(" ");
                    var validDate = !/Invalid|NaN/.test(new Date(stamp[0]).toString());
                    var validTime = /^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])(:([0-5]?[0-9]))?$/i.test(stamp[1]);
                    return this.optional(element) || (validDate && validTime);
                }, "Please enter a valid date and time.");
                $("#editForm").validate(
                        {
                            rules: {
                                bookName: {
                                    maxlength: 255
                                },
                                bookPrice: {
                                    number: true,
                                    min: 0
                                },
                                createTime: {
                                    dateTime : true
                                }
                            },
                            highlight: function (element) {
                                $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
                            },
                            unhighlight: function (element) {
                                $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
                            }
                        }
                );
            });
        </script>
    </tiles:putAttribute>
</tiles:insertDefinition>