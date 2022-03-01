import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

public class MD5Test {
    @Test
    public void md5Test1() {

        // 直接散列
        Md5Hash md5Hash0 = new Md5Hash("abc");
        System.out.println("散列后的结果:" + md5Hash0);

        // 为什么要加盐?
        // 为了让密码更加安全  更加不容易被破解

        Md5Hash md5Hashx = new Md5Hash("abc", "123");
        Md5Hash md5Hash1 = new Md5Hash("abc", "123");
        System.out.println("加盐散列之后的值:" + md5Hash1);
        System.out.println(md5Hashx);

        Md5Hash md5Hash2 = new Md5Hash("123abc");
        System.out.println("加盐散列之后的值:" + md5Hash2);
        //   System.out.println("md5Hash2 == md5Hash1 : " + md5Hash2 == md5Hash1);


        // param1：密码；param2：盐；param3：散列次数
        Md5Hash md5Hash = new Md5Hash("123", "", 1024);
        System.out.println("加盐+次数构成的值:" + md5Hash);

        Md5Hash md5Hash3 = new Md5Hash("123");
        Md5Hash md5Hash4 = new Md5Hash(md5Hash3);
        //   System.out.println("md5Hash == md5Hash4 : " + md5Hash == md5Hash4);
    }
}
