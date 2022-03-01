package idi.gorsonpy.controller;

import idi.gorsonpy.utils.Result;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//统一处理异常
@ControllerAdvice
public class ExceptionController {

    //这个地方 因为没前端不好做测试 所以简单地处理一下(处理所有异常)
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public Result<String> receiveEx(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        Result<String> result = Result.badRequest();
        if (e instanceof UnauthenticatedException) {
            System.out.println("未认证异常");
            result.setMessage("未认证异常");
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return result;
        } else if (e instanceof UnauthorizedException) {
            System.out.println("未授权异常");
            result.setMessage("未授权异常");
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return result;
        }

        //重定向错误视图
        response.sendRedirect(request.getContextPath() + "/error.jsp");
        return result;
    }
}
