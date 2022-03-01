package idi.gorsonpy.utils;

import java.util.UUID;

public class FileUtils {

    final static public String novelSuffixList = "txt";
    final static public String pictureSuffixList = "bmp,jpg,jpeg,png,gif";
    //检查文件的类型是否合法

    public static boolean checkTxtFileName(String fileName, String suffixList) {
        //截断点号直到末尾的部分就获得了文件的后缀名
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        return suffixList.trim().contains(suffix.toLowerCase());
    }

    public static String generateUid(String fileName) {
        return UUID.randomUUID().toString().replace("-", "") + fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }
}
