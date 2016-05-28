<%--
  Created by IntelliJ IDEA.
  User: eden90267
  Date: 2016/5/28
  Time: 下午4:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<tiles:insertDefinition name="defaultTemplate">
    <tiles:putAttribute name="title">
        Create
    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <h2>Create Book</h2>
        <sf:form id="createForm" method="post" commandName="book" modelattribute="book" enctype="multipart/form-data">
            <table cellspacing="0" class="pure-table">
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
                        <sf:input path="bookPrice" size="15"/>
                        <br/>
                        <sf:errors path="bookPrice" cssClass="error"/>
                    </td>
                </tr>
                <tr>
                    <th><label for="image">Book Image:</label></th>
                    <td>
                        <input id="image" name="image" type="file"/>
                        <br/>
                        <sf:errors path="bookImage" cssClass="error"/>
                    </td>
                </tr>
                <tr>
                    <th><sf:label path="createTime">Create Time:</sf:label></th>
                    <td>
                        <sf:input path="createTime" size="19" maxlength="19"/>
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
                        <input id="submit" type="submit" value="Create"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </sf:form>
        <script>
            $(document).ready(function () {
                $('ul.navbar-nav li').removeClass("active");
                $('ul.navbar-nav li:nth-child(4)').addClass("active");
                $("#createForm").validate(
                        {
                            rules: {
                                bookName: {
                                    remote: {
                                        url: "<s:url value='/book/validBookNameIsExists'/>",
                                        type: "get",
                                        data: {
                                            bookName: function () {
                                                return $("#bookName").val();
                                            }
                                        }
                                    }
                                }
                            },
                            highlight: function (element) {
                                $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
                            },
                            unhighlight: function (element) {
                                $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
                            },
                            messages: {
                                bookName: {
                                    remote: "Such bookName already exists!"
                                }
                            }
                        }
                );
            });
        </script>
    </tiles:putAttribute>
</tiles:insertDefinition>