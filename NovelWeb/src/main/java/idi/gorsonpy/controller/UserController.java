package idi.gorsonpy.controller;


import idi.gorsonpy.domain.User;
import idi.gorsonpy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public ModelAndView login(String username, String password) {
        System.out.println("进入到了login部分");
        ModelAndView modelAndView = new ModelAndView();
        System.out.println(username + password);
        User user = userService.Login(username, password);
        String result;
        if(user == null){
            modelAndView.setViewName("login");
            result = "error";
        }else{
            modelAndView.addObject("user", user);
            modelAndView.setViewName("success");
            result = "SUCCESS";
        }
        modelAndView.addObject("result", result);
        return modelAndView;
    }
}
