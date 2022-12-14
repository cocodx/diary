<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">
    function deleteTypeFunction(typeId){
        if (confirm("您确定要删除这条数据吗？")){
            window.location="diaryType?action=delete&typeId="+typeId;
        }
    }
</script>
<div class="data_list">
    <div class="data_list_title">
        <img src="${pageContext.request.contextPath}/images/list_icon.png"/>
        日记分类列表
    </div>
    <div style="margin: 10px">
        <button class="btn btn-primary" onclick="javascript:window.location='diaryType?action=preSave'">添加日记分类</button>
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
                    <td><button class="btn btn-info" onclick="javascript:window.location='diaryType?action=preSave&typeId=${diaryType.typeId}'">修改</button>&nbsp;&nbsp;<button class="btn btn-danger" onclick="deleteTypeFunction(${diaryType.typeId})">删除</button></td>
                </tr>
            </c:forEach>

        </tbody>
    </table>
    <div style="margin-top: 10px ;color: red;font-size: 18px;text-align: center;margin-left: auto;margin-right: auto"><p>${error}</p></div>
</div>
