package idi.gorsonpy.controller;

import idi.gorsonpy.utils.FileUtils;
import idi.gorsonpy.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;


@RequestMapping("/fileOp")
public class FileController {


    //上传txt文件
    @RequestMapping(value = "/uploadNovel")
    @ResponseBody
    public Result<String> uploadNovel(MultipartFile file, HttpServletRequest request){
        Result<String> result;
        String filename = file.getOriginalFilename();
        if(file.isEmpty() || "".equals(filename) || filename == null){
            result = Result.badRequest();
            result.setMessage("文件或文件名为空");
            return result;
        }
        if(FileUtils.checkTxtFileName(filename, FileUtils.novelSuffixList)){
            result = Result.badRequest();
            result.setMessage("文件名不符合要求");
            return result;
        }

        //文件存放路径位置
        String path = request.getSession().getServletContext().getRealPath("/upload/txt");
        File filepath = new File(path, filename);
        //如果路径不存在创建一个路径
        if(!filepath.exists()){
            boolean mkdirs = filepath.mkdirs();
        }
        try {
            file.transferTo(filepath);  //存储到路径下
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = Result.success(path);  //result的data域存放文件路径
        result.setMessage("小说上传成功");
        return result;
    }


    //上传图片文件
    @RequestMapping(value = "/uploadPicture")
    @ResponseBody
    public Result<String> uploadPicture(MultipartFile file, HttpServletRequest request){
        Result<String> result;
        String filename = file.getOriginalFilename();
        if(file.isEmpty() || "".equals(filename) || filename == null){
            result = Result.badRequest();
            result.setMessage("文件或文件名为空");
            return result;
        }
        if(FileUtils.checkTxtFileName(filename, FileUtils.pictureSuffixList)){
            result = Result.badRequest();
            result.setMessage("后缀名不符合要求");
            return result;
        }

        //文件存放路径位置
        String path = request.getSession().getServletContext().getRealPath("/upload/picture");
        File filepath = new File(path, filename);
        //如果路径不存在创建一个路径
        if(!filepath.exists()){
            boolean mkdirs = filepath.mkdirs();
        }
        try {
            file.transferTo(filepath);  //存储到路径下
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = Result.success(path);  //result的data域存放文件路径
        result.setMessage("图片上传成功");
        return result;
    }
}
