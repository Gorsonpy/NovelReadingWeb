package idi.gorsonpy.utils;

public class FileUtils {

    final static String suffixList = "txt, pdf";
    //检查文件的类型是否合法
    public static boolean checkFileName(String fileName){
        //截断点号直到末尾的部分就获得了文件的后缀名
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        return suffix.contains(suffixList.trim().toLowerCase());
    }
}
