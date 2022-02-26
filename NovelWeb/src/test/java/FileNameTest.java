import idi.gorsonpy.utils.FileUtils;

//后缀名检查测试
public class FileNameTest {
    public static void main(String[] args) {
        String filename = "check.txt";
        if(FileUtils.checkTxtFileName(filename, FileUtils.novelSuffixList)){
            System.out.println("正确");
        }else{
            System.out.println("错误");
        }
    }
}
