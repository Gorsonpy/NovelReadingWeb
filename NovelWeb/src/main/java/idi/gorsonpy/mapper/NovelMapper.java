package idi.gorsonpy.mapper;

import idi.gorsonpy.domain.Novel;
import idi.gorsonpy.domain.Type;
import idi.gorsonpy.utils.SearchInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface NovelMapper {

    //刚上传一本新的小说，下载次数都是0,状态都是未审核过
    @Insert("insert into novel values (null, #{name}, #{author}, #{description}, #{picturePath}, #{txtPath}, 0, #{type}, false)")
    void addNovel(@Param("name") String name, @Param("author") String author, @Param("description") String description, @Param("picturePath") String picturePath, @Param("txtPath") String txtPath, @Param("type") String type);


    @Select("select * from novel where is_checked = false")
    List<Novel> selectAll();

    @Insert("insert ignore into type values (null, #{typeName})")
    void addNovelType(@Param("typeName") String typeName);


    @Select("select * from type")
    List<Type> selectAllTypes();


    //涉及查询较为复杂，不适用注解的方式而是在xml里配置
    List<Novel> selectNovel(SearchInfo searchInfo);


    @Select("select * from novel where is_checked = false")
    List<Novel> selectUnchecked();


    @Update("update novel set is_checked = true where id = #{novelId}")
    void updateCheckedStatus(@Param("novelId") Long novelId);


    @Delete("delete from novel where id = #{novelId}")
    void delNovel(@Param("novelId") Long novelId);
}
