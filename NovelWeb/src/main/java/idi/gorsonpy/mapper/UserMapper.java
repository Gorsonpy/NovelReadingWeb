package idi.gorsonpy.mapper;

import idi.gorsonpy.domain.Favorites;
import idi.gorsonpy.domain.Novel;
import idi.gorsonpy.domain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    @Select("select * from user where username=#{username} and password=#{password}")
    User findUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Select("select * from user where username=#{username}")
    User selectUserByUsername(@Param("username") String username);

    @Insert("insert into user values (null, #{username}, #{password}, #{isAdmin})")
    void addUser(@Param("username") String username, @Param("password") String password, @Param("isAdmin") boolean isAdmin);

    @Select("select * from user")
    List<User> selectAll();


    //插入，如果已有该记录就忽略掉
    @Insert("insert ignore into favorites values (#{userId}, #{novelId})")
    void addUserNovelRel(@Param("userId") Long userId, @Param("novelId") Long novelId);

    @Select("select * from favorites where userId = #{userId} and novelId = #{novelId}")
    public Favorites selectFavoritesRel(@Param("userId") Long userId, @Param("novelId") Long novelId);

    @Delete("delete from favorites where userId = #{userId} and novelId = #{novelId}")
    void delUserNovelRel(@Param("userId") Long userId, @Param("novelId") Long novelId);

    @Select("select * from novel n, favorites f where f.userId = #{userId} and n.id = f.novelId")
    List<Novel> selectUserCollect(@Param("userId") Long userId);
}
