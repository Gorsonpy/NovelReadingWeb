package idi.gorsonpy.controller;


import idi.gorsonpy.domain.User;
import idi.gorsonpy.service.UserService;
import idi.gorsonpy.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Result<User> login(@RequestBody @RequestParam("username") String username, @RequestBody @RequestParam("password") String password, HttpServletRequest request) {
        User user = userService.Login(username, password);
        if(user == null) {
            Result<User> result = Result.badRequest();
            result.setMessage("用户名或密码错误");
            return result;
        }else{
            return Result.success(user);
        }
    }


    @RequestMapping("/register")
    public ModelAndView register(HttpServletRequest request, @RequestBody String username, @RequestBody String password, @RequestBody String checkCode){
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        HttpSession session = request.getSession();
        String CookieCheckCode = (String) session.getAttribute("checkCode");
        if(!CookieCheckCode.equals(checkCode)){
            modelAndView.setViewName("register");
            modelAndView.addObject("login_msg", "验证码错误");
            return modelAndView;
        }
        boolean b = userService.checkUserUsed(username);//检查该用户名是否被使用过
        if(b){
            modelAndView.addObject("login_msg", "用户名已经被使用");
            modelAndView.setViewName("register");
            return modelAndView;
        }
        userService.register(username, password);
        modelAndView.addObject("login_msg", "登录成功");
        modelAndView.setViewName("primaryView");
        return modelAndView;
    }


}
