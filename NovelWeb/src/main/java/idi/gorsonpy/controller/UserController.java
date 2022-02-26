package idi.gorsonpy.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import idi.gorsonpy.domain.User;
import idi.gorsonpy.service.UserService;
import idi.gorsonpy.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private Producer producer; //验证码生成器

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result<User> login(@RequestBody User u, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(u.getUsername() + u.getPassword());
        User user = userService.Login(u.getUsername(), u.getPassword());
        Result<User> result;
        if (user == null) {
            result = Result.badRequest();
            result.setMessage("用户名或密码错误");
            String result1 = JSON.toJSONString(result);
            System.out.println(result1);
            // request.getRequestDispatcher("/login.html").forward(request, response);
        } else {
            result = Result.success(user);
            result.setMessage("登录成功");
            String basepath = request.getContextPath() + "/success.html";
            // response.sendRedirect(request.getContextPath() + "/success.html");
            response.setHeader("REDIRECT", "REDIRECT");
            response.setHeader("CONTENTPATH", basepath);
        }
        return result;
    }


    //生成验证码并返回
    @RequestMapping(value = "/gerCheckCode", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public Result<String> getCheckCode(HttpServletRequest request) {
        String capText = producer.createText();
        HttpSession session = request.getSession();
        session.setAttribute("checkCode", capText);
        Result<String> result = null;
        try {
            //生成图形验证码
            BufferedImage bufferedImage = producer.createImage(capText);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jsp", byteArrayOutputStream);
            BASE64Encoder encoder = new BASE64Encoder();
            String img = encoder.encode(byteArrayOutputStream.toByteArray());
            //result的data域存放图片路径
            result = Result.success(img);
            result.setMessage("验证码生成成功");
        } catch (IOException e) {
            result = Result.badRequest();
            result.setMessage("验证码生成失败");
            e.printStackTrace();
        }
        return result;
    }


    //普通用户的注册
    @RequestMapping(value = "/register", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result<User> register(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        /*先检查验证码是否正确
     HttpSession session = request.getSession();
     String rightCheckCode = (String) session.getAttribute("checkCode");
        String checkCode = registerInfo.getString("checkCode");
        if (!rightCheckCode.equals(checkCode)) {
            Result<String> result = Result.badRequest();
            result.setMessage("答案错误");
            request.getRequestDispatcher("/register.html").forward(request, response);
            return result;
        } */
        // 检查用户名是否已经被使用过
        String username = user.getUsername();
        boolean b = userService.UserNameIsUsed(username);
        Result<User> result;
        if (b) {
            System.out.println("注册失败");
            result = Result.badRequest();
            result.setMessage("用户名已经使用");
        } else {
            //常规的注册方法，都是普通账号
            String password = user.getPassword();
            userService.register(username, password, false);
            System.out.println("注册成功");
            result = Result.success();
            result.setMessage("注册成功");
        }
        return result;
    }

    // 用户添加小说到收藏夹
    @RequestMapping("/collectNovel")
    @ResponseBody
    public Result<String> collectNovel(@RequestBody JSONObject jsonInfo){
        Long userId = jsonInfo.getLong("userId");
        Long novelId = jsonInfo.getLong("novelId");

        userService.collect(userId, novelId);

        return Result.success();
    }


}
