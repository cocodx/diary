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
import javax.servlet.http.HttpSession;
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
            HttpSession session = req.getSession();
            DiaryVo diaryVo = new DiaryVo();
            String page = req.getParameter("page");
            String typeId = req.getParameter("typeId");
            String queryMonth = req.getParameter("queryMonth");
            if (StringUtils.isNotEmpty(typeId)){
                diaryVo.setTypeId(Long.parseLong(typeId));
                session.setAttribute("typeId",typeId);
                session.removeAttribute("queryMonth");
            }
            if (StringUtils.isNotEmpty(queryMonth)){
                diaryVo.setQueryMonth(queryMonth);
                session.setAttribute("queryMonth",queryMonth);
                session.removeAttribute("typeId");
            }

            //从session中获取，上一次设置进去的参数
            if (StringUtils.isEmpty(typeId)){
                Object sessionTypeId = session.getAttribute("typeId");
                if (sessionTypeId != null) {
                    diaryVo.setTypeId(Long.parseLong((String) sessionTypeId));
                }
            }
            if (StringUtils.isEmpty(queryMonth)){
                Object sessionQueryMonth = session.getAttribute("queryMonth");
                if (sessionQueryMonth != null) {
                    diaryVo.setQueryMonth(String.valueOf(sessionQueryMonth));
                }
            }

            diaryVo.setSize(Long.parseLong(PropertiesUtils.getValue("pageSize")));
            if (StringUtils.isNotEmpty(page)){
                diaryVo.setPage(Long.parseLong(page));
            }
            //总数和日记列表
            Long total = diaryDao.selectCount(connection,diaryVo);
            List<Diary> diaries = diaryDao.selectList(connection,diaryVo);
            //日志分类数据，日志日期数据
            List<DiaryType> diaryTypes = diaryTypeDao.selectDiaryTypeCount(connection);
            List<MarkMonthData> markMonthData = diaryDao.selectMonthDataCount(connection);
            String pageCode = genPageCode(total,diaryVo);
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
    private String genPageCode(Long total,PageBean pageBean){
        pageBean.setTotal(total);
        System.out.println("total page:"+pageBean.getTotalPage());
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
