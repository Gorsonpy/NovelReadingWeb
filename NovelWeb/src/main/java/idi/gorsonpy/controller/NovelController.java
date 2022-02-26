package idi.gorsonpy.controller;


import com.alibaba.fastjson.JSONObject;
import idi.gorsonpy.domain.Novel;
import idi.gorsonpy.service.NovelService;
import idi.gorsonpy.utils.FileUtils;
import idi.gorsonpy.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/novel")
public class NovelController {
    @Autowired
    NovelService novelService;

    //上传小说
    @RequestMapping(value = "/uploadNovel")
    @ResponseBody
    public Result<String> uploadNovel(@RequestParam String name, @RequestParam String author, @RequestParam String description, @RequestParam MultipartFile pictureFile, @RequestParam MultipartFile txtFile, HttpServletRequest request) {
        System.out.println(author);
        System.out.println(name);
        System.out.println(description);
        Result<String> result = null;
        String pictureFileName = pictureFile.getOriginalFilename();
        String txtFileName = txtFile.getOriginalFilename();
        System.out.println(pictureFileName);
        System.out.println(txtFileName);

        //检查文件是否为空
        if (pictureFileName == null || pictureFileName.isEmpty() || txtFileName == null
                || txtFileName.isEmpty()) {
            result = Result.badRequest();
            result.setMessage("有未上传文件或有空文件名");
            return result;
        }
        if (!FileUtils.checkTxtFileName(txtFileName, FileUtils.novelSuffixList)) {
            result = Result.badRequest();
            result.setMessage("文件名不符合要求，小说内容仅支持txt后缀名");
            return result;
        }
        if (!FileUtils.checkTxtFileName(pictureFileName, FileUtils.pictureSuffixList)) {
            result = Result.badRequest();
            result.setMessage("文件名不符合要求，图片后缀名仅支持bmp,jpg,jpeg,png,gif");
            return result;
        }

        //文件存放路径位置
        String path1 = request.getSession().getServletContext().getRealPath("/waitingChecked/txt");
        String path2 = request.getSession().getServletContext().getRealPath("/waitingChecked/picture");

        File txtPath = new File(path1, txtFileName);
        File picturePath = new File(path2, pictureFileName);

        //如果路径不存在创建一个路径
        if (!picturePath.exists()) {
            boolean mkdirs = picturePath.mkdirs();
        }
        if (!txtPath.exists()) {
            boolean mkdirs = txtPath.mkdirs();
        }
        try {
            pictureFile.transferTo(picturePath);  //存储到路径下
            txtFile.transferTo(txtPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //调用业务层存储novel
        novelService.saveNovel(name, author, description, path2, path1);
        result = Result.success();
        result.setMessage("小说上传成功");
        return result;
    }



    //分页展示小说
    @RequestMapping(value = "/showNovels")
    @ResponseBody
    public Result<List<Novel>> showNovels(@RequestBody JSONObject pageInfo){
        Integer page = pageInfo.getInteger("page");
        Integer pageSize = pageInfo.getInteger("pageSize");
        List<Novel> novels = novelService.showByPage(page, pageSize);
        Result<List<Novel>> result = Result.success(novels);
        return result;
    }
}
