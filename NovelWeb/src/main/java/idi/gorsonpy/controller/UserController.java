package idi.gorsonpy.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import idi.gorsonpy.domain.User;
import idi.gorsonpy.service.UserService;
import idi.gorsonpy.utils.FileUtils;
import idi.gorsonpy.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
        if (user == null) {
            Result<User> result = Result.badRequest();
            result.setMessage("用户名或密码错误");
            return result;
        } else {
            Result<User> result = Result.success(user);
            result.setMessage("登录成功");
            return Result.success(user);
        }
    }


    @RequestMapping(value = "/gerCheckCode")
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
    public Result<String> register(@RequestBody JSONObject registerInfo, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //先检查验证码是否正确
        HttpSession session = request.getSession();
        String rightCheckCode = (String) session.getAttribute("checkCode");
        String checkCode = registerInfo.getString("checkCode");
        if (!rightCheckCode.equals(checkCode)) {
            Result<String> result = Result.badRequest();
            result.setMessage("答案错误");
            request.getRequestDispatcher("/register.html").forward(request, response);
            return result;
        }

        //检查用户名是否已经被使用过
        String username = registerInfo.getString("username");
        boolean b = userService.checkUserUsed(username);
        Result<String> result;
        if (b) {
            result = Result.badRequest();
            result.setMessage("用户名已经使用");
            request.getRequestDispatcher("/register.html").forward(request, response);
        } else {
            result = Result.success();
            result.setMessage("注册成功");
            String password = registerInfo.getString("password");
            userService.register(username, password);
            response.sendRedirect(request.getContextPath() + "/login.html");
        }
        return result;
    }

    @RequestMapping(value = "/saveNovel")
    @ResponseBody
    public Result<String> saveNovel(MultipartFile file, HttpServletRequest request){
        Result<String> result;
        if(file.isEmpty()){
            result = Result.badRequest();
            result.setMessage("文件为空");
            return result;
        }
        String filename = file.getOriginalFilename();
        assert filename != null;
        if(!FileUtils.checkFileName(filename)){
            result = Result.badRequest();
            result.setMessage("文件名不符合要求");
            return result;
        }
        HttpSession session = request.getSession();
        String path = "d:\\upload\\" + filename;
        File f = new File(path, filename);
        result = Result.success(path);
        result.setMessage("小说上传成功");
        return result;
    }



}
