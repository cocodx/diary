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
        String action = req.getParameter("action");
        Connection connection = null;
        try {
            connection = DbUtil.connection();
            if (action.equals("show")) {
                show(connection, req, resp);
            } else if (action.equals("delete")) {
                String typeId = req.getParameter("typeId");
                diaryTypeDao.deleteByTypeId(typeId, connection);
                show(connection, req, resp);
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private void show(Connection connection,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException {

        List<DiaryType> diaryTypes = diaryTypeDao.selectList(connection);
        request.setAttribute("diaryTypeList",diaryTypes);
        request.setAttribute("mainPage","diaryType/diaryTypeList.jsp");
        request.getRequestDispatcher("mainTemp.jsp").forward(request,response);

    }
}
