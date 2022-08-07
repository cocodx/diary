package io.github.cocodx.web;

import io.github.cocodx.dao.DiaryDao;
import io.github.cocodx.dao.DiaryTypeDao;
import io.github.cocodx.model.Diary;
import io.github.cocodx.model.DiaryType;
import io.github.cocodx.model.MarkMonthData;
import io.github.cocodx.model.PageBean;
import io.github.cocodx.model.vo.DiaryVo;
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

    private DiaryDao diaryDao = new DiaryDao();
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
            String page = req.getParameter("page");
            String typeId = req.getParameter("typeId");
            String queryMonth = req.getParameter("queryMonth");
            if (StringUtils.isNotEmpty(typeId)){

            }

            DiaryVo pageBean = new DiaryVo();
            pageBean.setSize(Long.parseLong(PropertiesUtils.getValue("pageSize")));
            if (StringUtils.isNotEmpty(page)){
                pageBean.setPage(Long.parseLong(page));
            }
            Long queryCount = null;
            if (StringUtils.isNotEmpty(typeId)) {
                pageBean.setTypeId(Long.parseLong(typeId));
                queryCount = Long.parseLong(typeId);
            }
            //总数和日记列表
            Long total = diaryDao.selectCount(connection,queryCount);
            List<Diary> diaries = diaryDao.selectList(connection,pageBean);
            //日志分类数据，日志日期数据
            List<DiaryType> diaryTypes = diaryTypeDao.selectDiaryTypeCount(connection);
            List<MarkMonthData> markMonthData = diaryDao.selectMonthDataCount(connection);
            String pageCode = genPageCode(total, pageBean,queryCount);
            req.setAttribute("pageCode",pageCode);
            req.setAttribute("diaryList",diaries);
            req.setAttribute("diaryTypes",diaryTypes);
            req.setAttribute("diaryDates",markMonthData);
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

    /**
     * 组装分页方法
     * @param total
     * @param pageBean
     * @Param typeId 日记类型id
     * @return
     */
    private String genPageCode(Long total,PageBean pageBean,Long typeId){
        pageBean.setTotal(total);
        System.out.println("total page:"+pageBean.getTotalPage());
        StringBuffer buffer = new StringBuffer();
        if (typeId!=null){
            buffer.append("<li><a href=\"main?page=1&typeId="+typeId+"\">首页</a></li>");
        }else {
            buffer.append("<li><a href=\"main?page=1\">首页</a></li>");
        }
        if (pageBean.hasPrev()){
            if (typeId!=null){
                buffer.append("<li><a href=\"main?page="+(pageBean.getPage().intValue()-1)+"&typeId="+typeId+"\">上一页</a></li>");
            }else{
                buffer.append("<li><a href=\"main?page="+(pageBean.getPage().intValue()-1)+"\">上一页</a></li>");
            }
        }else{
            buffer.append("<li><a href=\"#\" class=\"disabled\">上一页</a></li>");
        }
        Integer i=1;
        for (;;){
            if (i==pageBean.getPage().intValue()){
                if (typeId!=null){
                    buffer.append("<li><a href=\"main?page="+i+"&typeId="+typeId+"\" class=\"active\">"+i+"</a></li>");
                }else{
                    buffer.append("<li><a href=\"main?page="+i+"\" class=\"active\">"+i+"</a></li>");
                }
            }else {
                if (typeId!=null){
                    buffer.append("<li><a href=\"main?page="+i+"&typeId="+typeId+"\">"+i+"</a></li>");
                }else {
                    buffer.append("<li><a href=\"main?page="+i+"\">"+i+"</a></li>");
                }
            }
            if (i==pageBean.getTotalPage().intValue()){
                break;
            }
            i++;
        }
        if (pageBean.hasNext()){
            if (typeId!=null){
                buffer.append("<li><a href=\"main?page="+(pageBean.getPage().intValue()+1)+"&typeId="+typeId+"\">下一页</a></li>");
            }else{
                buffer.append("<li><a href=\"main?page="+(pageBean.getPage().intValue()+1)+"\">下一页</a></li>");
            }

        }else{
            buffer.append("<li><a href=\"#\" class=\"disabled\">下一页</a></li>");
        }
        if (typeId!=null){
            buffer.append("<li><a href=\"main?page="+pageBean.getTotalPage()+"&typeId="+typeId+"\">尾页</a></li>");
        }else {
            buffer.append("<li><a href=\"main?page="+pageBean.getTotalPage()+"\">尾页</a></li>");
        }
        return buffer.toString();
    }
}
