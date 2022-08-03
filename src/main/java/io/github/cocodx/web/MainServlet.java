package io.github.cocodx.web;

import io.github.cocodx.dao.DiaryDao;
import io.github.cocodx.model.Diary;
import io.github.cocodx.utils.DbUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * @author amazfit
 * @date 2022-08-01 下午11:43
 **/
public class MainServlet extends HttpServlet {

    DiaryDao diaryDao = new DiaryDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = null;
        try {
            connection = DbUtil.connection();
            List<Diary> diaries = diaryDao.selectList(connection);
            req.setAttribute("diaryList",diaries);
            req.setCharacterEncoding("UTF-8");
            req.setAttribute("mainPage","diary/diaryList.jsp");
            req.getRequestDispatcher("mainTemp.jsp").forward(req,resp);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                DbUtil.closeConnection(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
