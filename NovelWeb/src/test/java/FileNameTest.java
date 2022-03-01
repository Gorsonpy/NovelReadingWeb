import idi.gorsonpy.utils.FileUtils;
import org.junit.Test;

//后缀名检查测试
public class FileNameTest {
    public static void main(String[] args) {
        String filename = "check.txt";
        if (FileUtils.checkTxtFileName(filename, FileUtils.novelSuffixList)) {
            System.out.println("正确");
        } else {
            System.out.println("错误");
        }
    }

    //测试生成唯一ID
    @Test
    public void testGenerateId() {
        System.out.println(FileUtils.generateUid("xiaobai.txt"));
        System.out.println(FileUtils.generateUid("ll.jpg"));
    }

    @Test
    public void test() {
        System.out.println("\\\\");
    }
}
