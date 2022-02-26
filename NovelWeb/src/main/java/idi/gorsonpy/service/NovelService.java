package idi.gorsonpy.service;

import idi.gorsonpy.domain.Novel;

import java.util.List;

public interface NovelService {

    void saveNovel(String name, String author, String description, String picturePath, String txtPath);

    List<Novel> showByPage(int page, int pageSize);

}
