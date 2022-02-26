package idi.gorsonpy.mapper;

import idi.gorsonpy.domain.Novel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NovelMapper {
    @Insert("insert into novel values (null, #{name}, #{author}, #{description}, #{picturePath}, #{txtPath})")
    void addNovel(@Param("name") String name, @Param("author") String author, @Param("description") String description, @Param("picturePath") String picturePath, @Param("txtPath") String txtPath);


    @Select("select * from novel")
    List<Novel> selectAll();
}
