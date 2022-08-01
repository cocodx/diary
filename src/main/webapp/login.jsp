<%--
  Created by IntelliJ IDEA.
  User: amazfit
  Date: 2022-08-02
  Time: 上午6:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>个人日记本登录</title>
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/style/style.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/js/64d58efce2.js" crossorigin="anonymous"></script>
</head>
<body>
<div class="container">
    <div class="forms-container">
        <div class="signin-signup" >
            <form action="login" method="post" class="sign-in-form" onsubmit="return checkForm()">
                <h2 class="title">登录</h2>
                <div class="input-field">
                    <i class="fas fa-user"></i>
                    <input type="text" placeholder="用户名" id="userName" name="userName" value="${user.userName}">
                </div>
                <div class="input-field">
                    <i class="fas fa-lock"></i>
                    <input type="password" placeholder="密码" id="password" name="password" value="${user.password}">
                </div>
                <div class="input-field">
                    <label class="checkbox">
                        <input id="remember" name="remember" style="width: auto" type="checkbox" value="remember-me">
                        记住我&nbsp;&nbsp;&nbsp;&nbsp;<font id="error" color="red">${error}</font>
                    </label>
                </div>
                <input type="submit" value="登录" class="btn solid">

                <p class="social-text">或者使用社交平台登录</p>
                <div class="social-media">
                    <a href="#" class="social-icon">
                        <i class="fab fa-facebook-f"></i>
                    </a>
                    <a href="#" class="social-icon">
                        <i class="fab fa-twitter"></i>
                    </a>
                    <a href="#" class="social-icon">
                        <i class="fab fa-google"></i>
                    </a>
                    <a href="#" class="social-icon">
                        <i class="fab fa-linkedin-in"></i>
                    </a>
                </div>
            </form>

            <form action="" class="sign-up-form">
                <h2 class="title">注册</h2>
                <div class="input-field">
                    <i class="fas fa-user"></i>
                    <input type="text" placeholder="用户名">
                </div>
                <div class="input-field">
                    <i class="fas fa-envelope"></i>
                    <input type="text" placeholder="邮箱">
                </div>
                <div class="input-field">
                    <i class="fas fa-lock"></i>
                    <input type="password" placeholder="密码">
                </div>
                <input type="submit" value="注册" class="btn solid">

                <p class="social-text">或者使用社交平台登录</p>
                <div class="social-media">
                    <a href="#" class="social-icon">
                        <i class="fab fa-facebook-f"></i>
                    </a>
                    <a href="#" class="social-icon">
                        <i class="fab fa-twitter"></i>
                    </a>
                    <a href="#" class="social-icon">
                        <i class="fab fa-google"></i>
                    </a>
                    <a href="#" class="social-icon">
                        <i class="fab fa-linkedin-in"></i>
                    </a>
                </div>
            </form>
        </div>
    </div>

    <div class="panels-container">
        <div class="panel left-panel">
            <div class="content">
                <h3>一起吗 ?</h3>
                <p>只需要一两分钟，就可以注册成功，加入我们，别憋着，大声写出来吧...</p>
                <button class="btn transparent" id="sign-up-btn">注册</button>
            </div>

            <img src="images/log.svg" class="image" alt="">
        </div>

        <div class="panel right-panel">
            <div class="content">
                <h3>自己人吗 ?</h3>
                <p>快来看看有趣的事吧，也可以分享自己的生活乐趣哦！！！</p>
                <button class="btn transparent" id="sign-in-btn">登录</button>
            </div>

            <img src="images/register4.svg" class="image" alt="">
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/bootstrap/js/jQuery.js"></script>
<script src="${pageContext.request.contextPath}/js/app.js"></script>

</body>
</html>
