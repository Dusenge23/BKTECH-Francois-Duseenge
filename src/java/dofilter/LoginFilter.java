/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dofilter;

import domain.User;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Godwin
 */
@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //LoginController loginBean = (LoginController) ((HttpServletRequest) request).getSession().getAttribute("loginController");
        User userSession = (User) ((HttpServletRequest) request).getSession().getAttribute("userLoggedIn");
        String userType = (String) ((HttpServletRequest) request).getSession().getAttribute("userType");
        String contextPath = ((HttpServletRequest) request).getContextPath();
        HttpServletRequest req1 = ((HttpServletRequest) request);
        String currentPath = req1.getRequestURL().toString();
//
        if (currentPath.contains("admin") && userSession == null) {
            ((HttpServletResponse) response).sendRedirect(contextPath + "/faces/login.xhtml");
        } else if (currentPath.contains("customer") && userSession == null) {
            ((HttpServletResponse) response).
                    sendRedirect(contextPath + "/faces/login.xhtml");
        } 

        if (currentPath.contains("continueToPayment") && userSession == null) {
            ((HttpServletRequest) request).getSession().setAttribute("loginFrom", "continueToPayment");
            ((HttpServletResponse) response).
                    sendRedirect(contextPath + "/faces/login.xhtml");
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
