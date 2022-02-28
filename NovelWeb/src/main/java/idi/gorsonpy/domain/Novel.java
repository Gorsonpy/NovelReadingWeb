package idi.gorsonpy.domain;

import lombok.Data;

@Data
public class Novel {
    Long id;
    String name;

    //存放的是图片路径地址
    String picture;
    String author;
    String description;

    //存放的是小说内容的路径地址
    String disk;

    //存放的是小说属于哪一类型
    String type;

    //存放的该小说被下载的次数(推荐功能使用)
    Long times;

    //是否被审核通过了
    boolean isChecked;
}
