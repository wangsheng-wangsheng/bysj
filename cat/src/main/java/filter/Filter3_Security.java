package filter;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Filter 3", urlPatterns = {"/*"})
public class Filter3_Security implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        System.out.println("Filter 3 - security begins");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        String path= request.getRequestURI();
        JSONObject message = new JSONObject();
        HttpSession session = request.getSession(false);
        if (path.contains("/login") || path.contains("/logout")){
            chain.doFilter(servletRequest,servletResponse);
        }
        else if (session != null && session.getAttribute("currentUser") != null) {
            chain.doFilter(request, response);
            System.out.println("Filter 3 - session ends ");
        }
        else {
            message.put("message","您没有登录，请登录");
            response.getWriter().println(message);
            System.out.println("Filter 3 - session ends");
        }
    }

    @Override
    public void destroy() {

    }
}
