<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="data_list">
    <div class="data_list_title">
        <img src="${pageContext.request.contextPath}/images/list_icon.png"/>
        日记分类列表
    </div>
    <table class="table table-striped table-bordered table-hover">
        <thead>
            <tr>
                <th>日记分类ID</th>
                <th>日记分类名称</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>

            <c:forEach var="diaryType" items="${diaryTypeList}">
                <tr>
                    <td>${diaryType.typeId}</td>
                    <td>${diaryType.typeName}</td>
                    <td><button class="btn btn-info">修改</button>&nbsp;&nbsp;<button class="btn btn-danger">删除</button></td>
                </tr>
            </c:forEach>

        </tbody>
    </table>
</div>
