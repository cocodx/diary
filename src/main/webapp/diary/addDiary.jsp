<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script>
    function checkForm(){
        var title = document.getElementById("title").value;
        var content = CKEDITOR.instances.content.getData();
        var typeId = document.getElementById("typeId").value;
        if(title==null||title==""){
            document.getElementById("error").innerHTML="标题不能为空！";
            return false;
        }
        if(content==null||content==""){
            document.getElementById("error").innerHTML="内容不能为空！";
            return false;
        }
        if(typeId==null||typeId==""){
            document.getElementById("error").innerHTML="请选择日志类别！";
            return false;
        }
        return true;
    }
</script>
<div class="data_list">
    <div class="data_list_title">
        <img src="${pageContext.request.contextPath}/images/diary_add_icon.png"/>
        写日记
    </div>
    <form action="diary?action=save" method="post" onsubmit="return checkForm()">
        <div>
            <div class="diary_title">
                <input type="text" id="title" name="title" class="input-xlarge" value="${diary.title}" style="margin-top: 5px;height: 30px" placeholder="日志标题...">
            </div>
            <div>
                <textarea name="content" id="content" class="ckeditor">${diary.content}</textarea>
            </div>
            <div class="diary_type">
                <select name="typeId" id="typeId">
                    <option value="">请选择日志类别...</option>
                    <c:forEach var="diaryType" items="${diaryTypesList}">
                        <option value="${diaryType.typeId}" ${diaryType.typeId==diary.typeId?'selected':''}>${diaryType.typeName}</option>
                    </c:forEach>
                </select>
            </div>
            <div>
                <input type="hidden" id="diaryId" name="diaryId" value="${diary.diaryId }"/>
                <input type="submit" class="btn btn-primary" value="保存">
                <button class="btn-primary btn" type="button" onclick="javascript:history.back()">返回</button>
                <font  id="error" color="red">${error}</font>
            </div>
        </div>
    </form>

</div>
