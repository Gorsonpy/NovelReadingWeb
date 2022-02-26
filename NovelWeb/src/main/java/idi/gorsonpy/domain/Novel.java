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
}
