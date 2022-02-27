package idi.gorsonpy.mapper;

import idi.gorsonpy.domain.Novel;
import idi.gorsonpy.domain.Type;
import idi.gorsonpy.utils.SearchInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NovelMapper {

    //刚上传一本新的小说，下载次数都是0
    @Insert("insert into novel values (null, #{name}, #{author}, #{description}, #{picturePath}, #{txtPath}, 0, #{type})")
    void addNovel(@Param("name") String name, @Param("author") String author, @Param("description") String description, @Param("picturePath") String picturePath, @Param("txtPath") String txtPath, @Param("type") String type);


    @Select("select * from novel")
    List<Novel> selectAll();

    @Insert("insert ignore into type values (null, #{typeName})")
    void addNovelType(@Param("typeName") String typeName);


    @Select("select * from type")
    List<Type> selectAllTypes();


    //涉及查询较为复杂，不适用注解的方式而是在xml里配置
    List<Novel> selectNovel(SearchInfo searchInfo);
}
