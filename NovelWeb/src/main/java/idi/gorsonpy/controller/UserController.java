package idi.gorsonpy.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.code.kaptcha.Producer;
import idi.gorsonpy.domain.Favorites;
import idi.gorsonpy.domain.Novel;
import idi.gorsonpy.domain.User;
import idi.gorsonpy.service.UserService;
import idi.gorsonpy.utils.Page;
import idi.gorsonpy.utils.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private Producer producer; //验证码生成器

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result<User> login(@RequestBody JSONObject loginInfo, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String rememberStatus = loginInfo.getString("rememberStatus");
        String username = loginInfo.getString("username");
        String password = loginInfo.getString("password");
        //获取shrio框架实体
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        //设置记住我
        if ("1".equals(rememberStatus)) {
            usernamePasswordToken.setRememberMe(true);
        }
        try {
            //调用subject的login方法发送到安全管理器
            subject.login(usernamePasswordToken);
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            e.printStackTrace();
            return Result.badRequest();
        }
        return Result.success();
    }


    //退出登录
    @RequestMapping(value = "/logout")
    public String logOut(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        return "/login";
    }


    //生成验证码并返回
    @RequestMapping(value = "/getCheckCode", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Result<String> getCheckCode(HttpServletRequest request, HttpServletResponse response) {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = producer.createText();
        HttpSession session = request.getSession();
        session.setAttribute("checkCode", capText);
        Result<String> result;
        try {
            //生成图形验证码
            BufferedImage bufferedImage = producer.createImage(capText);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            ImageIO.write(bufferedImage, "jpg", servletOutputStream);
            servletOutputStream.flush();
            servletOutputStream.close();
            result = Result.success();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = Result.badRequest();
            return result;
        }
    }


    //普通用户的注册
    @RequestMapping(value = "/register", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result<String> register(@RequestBody JSONObject registerInfo, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 先检查验证码是否正确
        HttpSession session = request.getSession();
        String rightCheckCode = (String) session.getAttribute("checkCode");
        String checkCode = registerInfo.getString("checkCode");


        //验证码的比较忽略大小写
        if (!rightCheckCode.equalsIgnoreCase(checkCode)) {
            Result<String> result = Result.badRequest();
            result.setMessage("验证码错误");
            request.getRequestDispatcher("/register.html").forward(request, response);
            return result;
        }
        // 检查用户名是否已经被使用过
        String username = registerInfo.getString("username");
        boolean b = userService.UserNameIsUsed(username);
        Result<String> result;
        if (b) {
            System.out.println("注册失败");
            result = Result.badRequest();
            result.setMessage("用户名已经使用");
        } else {
            //常规的注册方法，都是普通账号
            String password = registerInfo.getString("password");

            //为了避免明文密码，这里使用Md5加盐加密后在存储到数据库
            Object salt = ByteSource.Util.bytes(username);
            //把用户名作为盐
            /*
             * 1.加密方法
             * 2.加密对象
             * 3.盐
             * 4.加密次数
             */
            SimpleHash simpleHash = new SimpleHash("MD5", password, salt, 1024);

            //传的是加密后的密码而不是原始密码
            userService.register(username, simpleHash.toString(), false);
            System.out.println("注册成功");
            result = Result.success();
            result.setMessage("注册成功");
        }
        return result;
    }

    //用户显示某小说是否收藏状态
    @RequiresRoles(value = {"admin", "general"}, logical = Logical.OR)
    @RequestMapping(value = "/showCollectStatus", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result<String> showCollectStatus(@RequestBody Favorites favorites) {
        Long userId = favorites.getUserId();
        Long novelId = favorites.getNovelId();
        boolean b = userService.IsCollect(userId, novelId);
        Result<String> result;
        if (b) {
            result = Result.success();
            result.setMessage("已收藏");
        } else {
            result = Result.badRequest();
            result.setMessage("未收藏");
        }
        return result;
    }

    // 用户添加小说到收藏夹
    @RequiresRoles(value = {"admin", "general"}, logical = Logical.OR)
    @RequestMapping(value = "/collectNovel", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result<String> collectNovel(@RequestBody Favorites favorites) {
        Long userId = favorites.getUserId();
        Long novelId = favorites.getNovelId();

        userService.collect(userId, novelId);
        return Result.success();
    }

    //用户取消收藏
    @RequiresRoles(value = {"admin", "general"}, logical = Logical.OR)
    @RequestMapping(value = "/delCollect", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result<String> delCollect(@RequestBody JSONObject delInfo) {
        Long userId = delInfo.getLong("userId");
        Long novelId = delInfo.getLong("novelId");
        userService.delCollect(userId, novelId);
        return Result.success();
    }

    //用户收藏夹里小说展示
    @RequiresRoles(value = {"admin", "general"}, logical = Logical.OR)
    @RequestMapping(value = "/showCollectNovels", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result<List<Novel>> showCollectNovels(@RequestBody JSONObject userInfo) {
        Long userId = userInfo.getLong("userId");
        List<Novel> novels = userService.selectUserCollect(userId);

        if (novels != null)
            return Result.success(novels);
        else
            return Result.badRequest();
    }


    @RequiresRoles(value = {"admin"})
    // 展示待审核的小说
    @RequestMapping(value = "/showUnchecked", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Page<List<Novel>> showUnchecked(@RequestBody JSONObject pageInfo) {
        Integer page = pageInfo.getInteger("page");
        Integer pageSize = pageInfo.getInteger("pageSize");
        List<Novel> novels = userService.showNovels(page, pageSize);
        Page<List<Novel>> p = Page.success(novels);
        PageInfo<Novel> pInfo = new PageInfo<Novel>(novels);
        p.setPageInfo(pInfo);
        return p;
    }


    @RequiresRoles(value = {"admin"})
    // 审核小说
    @RequestMapping(value = "/checkNovels", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
    @ResponseBody
    private void checkNovels(@RequestBody JSONObject checkInfo) {
        String checkStatus = checkInfo.getString("checkStatus");
        Long novelId = checkInfo.getLong("novelId");
        userService.checkNovels(novelId, checkStatus);
    }


}
