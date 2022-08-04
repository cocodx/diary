package io.github.cocodx.web;

import io.github.cocodx.dao.DiaryDao;
import io.github.cocodx.model.Diary;
import io.github.cocodx.model.PageBean;
import io.github.cocodx.utils.DbUtil;
import io.github.cocodx.utils.PropertiesUtils;
import io.github.cocodx.utils.StringUtils;

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
            String page = req.getParameter("page");
            PageBean pageBean = new PageBean();
            pageBean.setSize(Long.parseLong(PropertiesUtils.getValue("pageSize")));
            if (StringUtils.isNotEmpty(page)){
                pageBean.setPage(Long.parseLong(page));
            }
            List<Diary> diaries = diaryDao.selectList(connection,pageBean);
            Long total = diaryDao.selectCount(connection);
            String pageCode = genPageCode(total, pageBean);
            req.setAttribute("pageCode",pageCode);
            req.setAttribute("diaryList",diaries);
            req.setCharacterEncoding("UTF-8");
            req.setAttribute("mainPage","diary/diaryList.jsp");
            req.getRequestDispatcher("mainTemp.jsp").forward(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DbUtil.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String genPageCode(Long total,PageBean pageBean){
        pageBean.setTotal(total);
        StringBuffer buffer = new StringBuffer();
        buffer.append("<li><a href=\"main?page=1\">首页</a></li>");
        if (pageBean.hasPrev()){
            buffer.append("<li><a href=\"main?page="+(pageBean.getPage().intValue()-1)+"\">上一页</a></li>");
        }else{
            buffer.append("<li><a href=\"#\" class=\"disabled\">上一页</a></li>");
        }
        Integer i=1;
        for (;;){
            if (i==pageBean.getPage().intValue()){
                buffer.append("<li><a href=\"main?page="+i+"\" class=\"active\">"+i+"</a></li>");
            }else {
                buffer.append("<li><a href=\"main?page="+i+"\">"+i+"</a></li>");
            }
            if (i==pageBean.getTotalPage().intValue()){
                break;
            }
            i++;
        }
        if (pageBean.hasNext()){
            buffer.append("<li><a href=\"main?page="+(pageBean.getPage().intValue()+1)+"\">下一页</a></li>");
        }else{
            buffer.append("<li><a href=\"#\" class=\"disabled\">下一页</a></li>");
        }
        buffer.append("<li><a href=\"main?page="+pageBean.getTotalPage()+"\">尾页</a></li>");
        return buffer.toString();
    }
}
