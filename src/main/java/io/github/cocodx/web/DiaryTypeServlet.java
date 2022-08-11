package io.github.cocodx.web;

import io.github.cocodx.dao.DiaryTypeDao;
import io.github.cocodx.model.DiaryType;
import io.github.cocodx.utils.DbUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author amazfit
 * @date 2022-08-11 下午9:03
 **/
public class DiaryTypeServlet extends HttpServlet {

    private DiaryTypeDao diaryTypeDao = new DiaryTypeDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = null;
        try {
            connection = DbUtil.connection();
            List<DiaryType> diaryTypes = diaryTypeDao.selectList(connection);
            req.setAttribute("diaryTypeList",diaryTypes);
            req.setAttribute("mainPage","diaryType/diaryTypeList.jsp");
            req.getRequestDispatcher("mainTemp.jsp").forward(req,resp);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
