package idi.gorsonpy.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import idi.gorsonpy.domain.Novel;
import idi.gorsonpy.service.NovelService;
import idi.gorsonpy.utils.FileUtils;
import idi.gorsonpy.utils.Page;
import idi.gorsonpy.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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


        //检查后把名字换成唯一标识性ID
        pictureFileName = FileUtils.generateUid(pictureFileName);
        txtFileName = FileUtils.generateUid(txtFileName);

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


    //分页展示小说除图片之外的信息
    @RequestMapping(value = "/showNovels")
    @ResponseBody
    public Page<List<Novel>> showNovels(@RequestBody JSONObject pageInfo) {
        Integer page = pageInfo.getInteger("page");
        Integer pageSize = pageInfo.getInteger("pageSize");
        List<Novel> novels = novelService.showByPage(page, pageSize);
        PageInfo<Novel> novelPageInfo = new PageInfo<Novel>(novels);
        Page<List<Novel>> novelPage = Page.success(novels);
        novelPage.setPageInfo(novelPageInfo);
        return novelPage;
    }

    //下载文件
    @RequestMapping(value = "/download/File")
    @ResponseBody
    public Result<String> showPicture(@RequestBody JSONObject filePathInfo, HttpServletResponse response) throws IOException {
        String filePath = filePathInfo.getString("filePath");

        response.reset(); //设置不缓存
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data"); //设置以二进制方式交换数据

        File file = new File(filePath, filePath);
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024]; //每次传输1024字节

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
            outputStream.flush();
        }
        outputStream.close();
        inputStream.close();
        return Result.success();
    }

    //

}
