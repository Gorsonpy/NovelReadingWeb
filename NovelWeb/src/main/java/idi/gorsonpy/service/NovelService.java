package idi.gorsonpy.service;

import idi.gorsonpy.domain.Novel;
import idi.gorsonpy.domain.Type;
import idi.gorsonpy.utils.SearchInfo;

import java.util.List;

public interface NovelService {

    void saveNovel(String name, String author, String description, String picturePath, String txtPath, String type);

    List<Novel> showByPage(int page, int pageSize);

    void addNovelType(String typeName);

    List<Type> showTypes();

    List<Novel> selectNovel(SearchInfo searchInfo);

    List<Novel> showPopular();

    void addTimes(String filePath);
}
