package cn.edu.sdjzu.xg.bysj.controller.basic.security;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        JSONObject message = new JSONObject();
        try {
            User loggedUser = UserService.getInstance().login(username,password);
            if (loggedUser != null){
                System.out.println("1111111111111111111111111111111111111111111");
                message.put("message","登录成功");
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(10*60);
                session.setAttribute("currentUser",loggedUser);
            }else {
                message.put("message","用户名或密码输入错误");
            }
        } catch (SQLException e) {
            message.put("message","数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message","网络异常");
            e.printStackTrace();
        }
        response.getWriter().println(message);
    }
}
