package idi.gorsonpy.utils;


import lombok.Data;

//用户搜索信息的封装类
@Data
public class SearchInfo {
    private String name;
    private String author;
    private String type;
    private String description;
}
