package io.github.cocodx.web;

import io.github.cocodx.dao.DiaryDao;
import io.github.cocodx.dao.DiaryTypeDao;
import io.github.cocodx.model.DiaryType;
import io.github.cocodx.utils.DbUtil;
import io.github.cocodx.utils.StringUtils;

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
    private DiaryDao diaryDao = new DiaryDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        Connection connection = null;
        try {
            connection = DbUtil.connection();
            if (action.equals("show")) {
                show(connection, req, resp);
            } else if (action.equals("delete")) {
                String typeId = req.getParameter("typeId");
                //判断 diaryCount不大于0 就删除
                Integer count = diaryDao.selectCountByTypeId(typeId,connection);
                if(count>0){
                    req.setAttribute("error","当前日记分类下有日记数据，不能删除！");
                }else{
                    diaryTypeDao.deleteByTypeId(typeId, connection);
                }
                show(connection, req, resp);
            } else if (action.equals("preSave")){
                String typeId = req.getParameter("typeId");
                if (StringUtils.isNotEmpty(typeId)){
                    DiaryType diaryType = diaryTypeDao.selectOne(typeId,connection);
                    req.setAttribute("diaryType",diaryType);
                }
                req.setAttribute("mainPage","diaryType/diaryTypeInfo.jsp");
                req.getRequestDispatcher("mainTemp.jsp").forward(req,resp);
            } else if (action.equals("save")){
                String typeId = req.getParameter("typeId");
                String typeName = req.getParameter("typeName");
                if (StringUtils.isNotEmpty(typeId)){
                    diaryTypeDao.update(typeId,typeName, connection);
                }else{
                    diaryTypeDao.insert(typeName,connection);
                }

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
