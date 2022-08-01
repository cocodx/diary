package io.github.cocodx.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author amazfit
 * @date 2022-08-01 下午11:55
 **/
@Deprecated
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        Object currentUser = session.getAttribute("currentUser");
        String servletPath = request.getServletPath();
        if(currentUser==null && !servletPath.contains("login") && !servletPath.contains("bootstrap") && !servletPath.contains("style") && !servletPath.contains("js") && !servletPath.contains("img")){
            response.sendRedirect("login.jsp");
        }else{
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
