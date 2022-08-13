package io.github.cocodx.web;

import io.github.cocodx.dao.UserDao;
import io.github.cocodx.model.User;
import io.github.cocodx.utils.DateUtils;
import io.github.cocodx.utils.DbUtil;
import io.github.cocodx.utils.Md5Utils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author amazfit
 * @date 2022-08-13 上午1:15
 **/
public class MeServlet extends HttpServlet {

    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = null;
        String action = req.getParameter("action");
        try {
            connection = DbUtil.connection();
            if (action.equals("show")){
                show(connection,req,resp);
            }else if (action.equals("save")){
                save(connection,req,resp);
            }else if (action.equals("getImages")){
                getImages(req,resp);
            }
        } catch (Exception e) {
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
     * 获取 图片文件流
     * @param req
     * @param resp
     */
    private void getImages(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String imageName = req.getParameter("imageName");
        resp.setContentType("image/jpeg");
        resp.setDateHeader("expries", -1);
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Pragma", "no-cache");
        String replace = imageName.replace("/diary/userImagePath/", "");
        FileInputStream fis = new FileInputStream("D:\\code\\IDEA_workspace\\diary\\src\\main\\webapp\\userImagePath\\"+replace);
        File file = new File("D:\\code\\IDEA_workspace\\diary\\src\\main\\webapp\\userImagePath\\"+replace);
        if (file.exists()){
            IOUtils.copy(fis,resp.getOutputStream());
        }
        fis.close();
    }

    private void save(Connection connection, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(4096);
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        List<FileItem> fileItems = servletFileUpload.parseRequest(req);
        User user = new User();
        if (fileItems!=null && fileItems.size()>0){
            for(FileItem fileItem:fileItems){
                //普通表单
                if (fileItem.isFormField()){
                    String fileName = fileItem.getFieldName();
                    if (fileName.equals("nickName")){
                        user.setNickName(fileItem.getString("utf-8"));
                    }
                    if (fileName.equals("mood")){
                        user.setMood(fileItem.getString("utf-8"));
                    }
                    if (fileName.equals("userId")){
                        user.setUserId(Long.valueOf(fileItem.getString("utf-8")));
                    }
                } else if (!"".equals(fileItem.getName())) {
                    //上传到一个文件路径里
                    String imageName = DateUtils.SIMPLE_UUID();
                    user.setImageName(imageName+"."+fileItem.getName().split("\\.")[1]);
                    String imagePath = "D:\\code\\IDEA_workspace\\diary\\src\\main\\webapp\\userImagePath\\"+user.getImageName();
                    File file = new File(imagePath);
                    fileItem.write(file);
                    user.setImageName("/diary/userImagePath/"+user.getImageName());
                }
            }
            userDao.update(connection,user);
        }
        HttpSession session = req.getSession();
        session.setAttribute("currentUser",user);
        show(connection,req,resp);
    }

    private void show(Connection connection,HttpServletRequest req,HttpServletResponse resp) throws Exception {
        Cookie[] cookies = req.getCookies();
        String value = null;
        for (Cookie cookie:cookies){
            if (cookie.getName().equals("user")){
                value = cookie.getValue();
            }
        }
        String userName = value.split("-")[0];
        String password = value.split("-")[1];
        User user = userDao.selectOne(connection,userName, Md5Utils.md5(password));
        req.setAttribute("userInfo",user);
        req.setAttribute("mainPage","me/mine.jsp");
        req.getRequestDispatcher("mainTemp.jsp").forward(req,resp);
    }
}
