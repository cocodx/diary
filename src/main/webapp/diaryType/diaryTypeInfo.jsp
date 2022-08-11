<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">
    function deleteFunction(diaryId){
        if (confirm("您确定要删除这个日志吗？")){
            window.location="diary?action=delete&diaryId="+diaryId;
        }
    }
    function checkFrom(){
        var typeName = document.getElementById("typeName");
        if(typeName==null||typeName==""){
            document.getElementById("error").innerHTML="日记类型名称不能为空！";
            return false;
        }
        return true;
    }
</script>
<div class="data_list">
    <div class="data_list_title">
        <img src="${pageContext.request.contextPath}/images/diary_type_edit_icon.png"/>
        日记分类信息
    </div>
    <form class="form-horizontal" method="post" action="diaryType?action=save">
        <div class="control-group" style="margin-top: 20px">
            <label class="control-label" for="inputEmail">日记类型名称</label>
            <div class="controls">
                <input type="text" name="typeId" placeholder="typeName" style="display: none" value="${diaryType.typeId}">
                <input type="text" id="typeName" name="typeName" placeholder="typeName" value="${diaryType.typeName}">
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn btn-success" onsubmit="return checkForm()">保存</button>
                <font  id="error" color="red">${error}</font>
            </div>
        </div>
    </form>
</div>
