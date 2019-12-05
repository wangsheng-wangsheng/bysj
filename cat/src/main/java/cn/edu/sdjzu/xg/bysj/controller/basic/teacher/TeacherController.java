package cn.edu.sdjzu.xg.bysj.controller.basic.teacher;

import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * 将所有方法组织在一个Controller(Servlet)中
 */
@WebServlet("/teacher.ctl")
public class TeacherController extends HttpServlet {
    //请使用以下JSON测试增加功能（id为空）
    //{"description":"id为null的新学院","no":"05","remarks":""}
    //请使用以下JSON测试修改功能
    //{"description":"修改id=1的学院","id":1,"no":"05","remarks":""}

    /**
     * POST, http://localhost:8080/Teacher.ctl, 增加学院
     * 增加一个学院对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request 请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        //根据request对象，获得代表参数的JSON字串
        System.out.println("22222222222222222222222222222222");
        String Teacher_json = JSONUtil.getJSON(request);

        //将JSON字串解析为Teacher对象
        Teacher TeacherToAdd = JSON.parseObject(Teacher_json, Teacher.class);

        //设置响应字符编码为UTF-8
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加Teacher对象
        System.out.println("333333333333333333333333333333333");
        try {
            System.out.println("444444444444444444444444444");
            if (TeacherService.getInstance().add(TeacherToAdd)) {
                message.put("message", "添加成功");
                System.out.println("1111111111111111111111111111111111111");
            }
            else {
                message.put("message", "添加失败");
            }
        }catch (SQLException e){
            e.printStackTrace();
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            e.printStackTrace();
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * DELETE, http://localhost:8080/Teacher.ctl?id=1, 删除id=1的学院
     * 删除一个学院对象：根据来自前端请求的id，删除数据库表中id的对应记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);

        //设置响应字符编码为UTF-8
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();

        //到数据库表中删除对应的学院
        try {
            if(TeacherService.getInstance().delete(id))
                message.put("message", "删除成功");
            else
                message.put("message","删除失败");

        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }


    /**
     * PUT, http://localhost:8080/Teacher.ctl, 修改学院
     *
     * 修改一个学院对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        String Teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        Teacher TeacherToAdd = JSON.parseObject(Teacher_json, Teacher.class);
        //设置响应字符编码为UTF-8
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表修改Teacher对象对应的记录
        try {
            TeacherService.getInstance().update(TeacherToAdd);
            message.put("message", "修改成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * GET, http://localhost:8080/Teacher.ctl?id=1, 查询id=1的学院
     * GET, http://localhost:8080/Teacher.ctl, 查询所有的学院
     * 把一个或所有学院对象响应到前端
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        //读取参数id
        String id_str = request.getParameter("id");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                responseTeachers(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseTeacher(id, response);
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            //响应message到前端
            response.getWriter().println(message);
        }catch(Exception e){
            message.put("message", "网络异常");
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    //响应一个学院对象
    private void responseTeacher(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        Teacher Teacher = TeacherService.getInstance().find(id);
        String Teacher_json = JSON.toJSONString(Teacher);

        //响应Teacher_json到前端
        response.getWriter().println(Teacher_json);
    }
    //响应所有学院对象
    private void responseTeachers(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<Teacher> Teachers = TeacherService.getInstance().findAll();
        String Teachers_json = JSON.toJSONString(Teachers, SerializerFeature.DisableCircularReferenceDetect);

        //响应Teachers_json到前端
        response.getWriter().println(Teachers_json);
    }
}