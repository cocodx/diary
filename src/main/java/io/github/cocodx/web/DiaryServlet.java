package io.github.cocodx.web;

import io.github.cocodx.dao.DiaryDao;
import io.github.cocodx.dao.DiaryTypeDao;
import io.github.cocodx.model.Diary;
import io.github.cocodx.model.DiaryType;
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
 * 日记详情页 查看详情页，修改，删除。
 * @author amazfit
 * @date 2022-08-08 下午11:00
 **/
public class DiaryServlet extends HttpServlet {

    private DiaryDao diaryDao = new DiaryDao();
    private DiaryTypeDao diaryTypeDao = new DiaryTypeDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String diaryId = request.getParameter("diaryId");
        String action = request.getParameter("action");
        if (action.equals("show")){
            show(request,response,diaryId);
        }else if(action.equals("add")){
            add(request,response);
        }else if (action.equals("save")){
            save(request,response);
        }else if (action.equals("delete")){
            delete(request,response);
        }
    }

    /**
     * 删除日记
     * @param request
     * @param response
     */
    private void delete(HttpServletRequest request, HttpServletResponse response) {
        String diaryId = request.getParameter("diaryId");
        Connection connection = null;
        try {
            connection = DbUtil.connection();
            diaryDao.delete(connection,diaryId);
            request.getRequestDispatcher("main?all=true").forward(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    DbUtil.closeConnection(connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    /**
     * 保存日记，跳转list界面
     * @param request
     * @param response
     */
    private void save(HttpServletRequest request, HttpServletResponse response) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String typeId = request.getParameter("typeId");
        Connection connection = null;
        try {
            connection = DbUtil.connection();
            diaryDao.insert(connection,title,content,typeId);
            request.getRequestDispatcher("main?all=true").forward(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (connection!=null){
                try {
                    DbUtil.closeConnection(connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void show(HttpServletRequest request, HttpServletResponse response,String diaryId){
        Connection connection = null;
        try {
            connection = DbUtil.connection();
            Diary diary = diaryDao.selectOne(connection, diaryId);
            request.setAttribute("diary",diary);
            request.setAttribute("mainPage", "diary/diaryInfo.jsp");
            request.getRequestDispatcher("mainTemp.jsp").forward(request,response);
        } catch (ClassNotFoundException | SQLException | ParseException | ServletException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection!=null){
                try {
                    DbUtil.closeConnection(connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 去写日记的界面
     * @param request
     * @param response
     */
    private void add(HttpServletRequest request,HttpServletResponse response){
        Connection connection = null;
        try {
            connection = DbUtil.connection();
            List<DiaryType> diaryTypes = diaryTypeDao.selectList(connection);
            request.setAttribute("diaryTypes",diaryTypes);
            request.setAttribute("mainPage", "diary/addDiary.jsp");
            request.getRequestDispatcher("mainTemp.jsp").forward(request,response);
        } catch (ServletException | IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            if (connection != null) {
                try {
                    DbUtil.closeConnection(connection);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
