package io.github.cocodx.web;

import io.github.cocodx.dao.UserDao;
import io.github.cocodx.model.User;
import io.github.cocodx.utils.DbUtil;
import io.github.cocodx.utils.Md5Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author amazfit
 * @date 2022-08-01 上午6:20
 **/
public class LoginServlet extends HttpServlet {

    UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String encryptPassword = Md5Utils.md5(password);
        Connection connection = null;
        try {
            connection = DbUtil.connection();

            User user = userDao.selectOne(connection,userName,encryptPassword);
            if (user==null){
                user = new User();
                user.setUserName(userName);
                user.setPassword(password);
                request.setAttribute("user",user);
                request.setAttribute("error","用户名或者密码不存在");
                request.getRequestDispatcher("login.jsp").forward(request,response);
            }else{
                session.setAttribute("currentUser",user);
                response.sendRedirect("main.jsp");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                DbUtil.closeConnection(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
