import idi.gorsonpy.mapper.UserMapper;
import idi.gorsonpy.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:applicationContext.xml")
public class MySqlTest {
    @Autowired
    private UserMapper userMapper;

    //测试mysql基本链接
    @Test
    public void testMysqlConn(){
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        System.out.println(mapper);
    }

    //测试Spring注入方式连接
    @Test
    public void SpringMybatisConn(){
        System.out.println(userMapper);
    }
}
