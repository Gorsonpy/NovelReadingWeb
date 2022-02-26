package idi.gorsonpy.mapper;

import idi.gorsonpy.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    @Select("select * from user where username=#{username} and password=#{password}")
    User findUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Select("select * from user where username=#{username}")
    User selectUserByUsername(String username);

    @Insert("insert into user values (null, #{username}, #{password}, #{isAdmin})")
    void addUser(@Param("username") String username, @Param("password") String password, @Param("isAdmin") boolean isAdmin);

    @Select("select * from user")
    List<User> selectAll();
}
