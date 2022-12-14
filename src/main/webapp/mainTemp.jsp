<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>个人日记本主页</title>
<link href="${pageContext.request.contextPath}/style/diary.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/style/animate.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/bootstrap/js/jQuery.js"></script>
<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/js/ckeditor/ckeditor.js"></script>
</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="brand" href="#">屌丝的日记本</a>
            <div class="nav-collapse collapse">
                <ul class="nav">
                    <li class="active"><a href="main?all=true"><i class="icon-home"></i>&nbsp;主页</a></li>
                    <li class="active"><a href="diary?action=add"><i class="icon-pencil"></i>&nbsp;写日记</a></li>
                    <li class="active"><a href="diaryType?action=show"><i class="icon-book"></i>&nbsp;日记分类管理</a></li>
                    <li class="active"><a href="me?action=show"><i class="icon-user"></i>&nbsp;个人中心</a></li>
                </ul>
            </div>
            <form name="myForm" class="navbar-form pull-right" method="post" action="main?all=true">
                <input type="text" class="span2" id="s_title" name="s_title" style="margin-top:5px ;height: 30px" placeholder="往事如烟...">
                <button type="submit" class="btn" onkeydown="if (event.keyCode==13) myForm.submit()"><i class="icon-search"></i>&nbsp;Submit</button>
            </form>
        </div>
    </div>
</div>

<div class="container">
    <div class="row-fluid">
        <div class="span9">
            <jsp:include page="${mainPage}"></jsp:include>
        </div>
        <div class="span3">
            <div class="data_list">
                <div class="data_list_title">
                    <img src="${pageContext.request.contextPath}/images/user_icon.png"/>
                    个人中心
                </div>
                <div class="user_image animate__animated animate__shakeX">
                    <img src="me?action=getImages&imageName=${currentUser.imageName}" alt="">
                </div>
                <div class="nickName" style="padding-top:10px ">${currentUser.nickName}</div>
                <div class="animate__animated animate__shakeY userSign">${currentUser.mood}</div>
            </div>

            <div class="data_list">
                <div class="data_list_title">
                    <img src="${pageContext.request.contextPath}/images/byType_icon.png"/>
                    按日志类别
                </div>
                <div class="dataTypes">
                    <ul>
                        <c:forEach var="diary" items="${diaryTypes}">
                            <li>
                                <a href="main?page=1&typeId=${diary.typeId}">${diary.typeName}（${diary.count}）</a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <div class="data_list">
                <div class="data_list_title">
                    <img src="${pageContext.request.contextPath}/images/byDate_icon.png"/>
                    按日志日期
                </div>
                <div class="dataTypes">
                    <ul>
                        <c:forEach var="diaryDate" items="${diaryDates}">
                            <li>
                                <a href="main?page=1&queryMonth=${diaryDate.dataMonth}">${diaryDate.dataMonth}（${diaryDate.dataNumber}）</a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>