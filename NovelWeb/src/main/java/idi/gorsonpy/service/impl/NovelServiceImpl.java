package idi.gorsonpy.service.impl;


import com.github.pagehelper.PageHelper;
import idi.gorsonpy.domain.Novel;
import idi.gorsonpy.domain.Type;
import idi.gorsonpy.mapper.NovelMapper;
import idi.gorsonpy.service.NovelService;
import idi.gorsonpy.utils.SearchInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("novelService")
public class NovelServiceImpl implements NovelService {
    @Autowired
    NovelMapper novelMapper;

    @Override
    public void saveNovel(String name, String author, String description, String picturePath, String txtPath, String type) {
        novelMapper.addNovel(name, author, description, picturePath, txtPath, type);
    }

    @Override
    public List<Novel> showByPage(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return novelMapper.selectAll();
    }

    @Override
    public void addNovelType(String typeName) {
        novelMapper.addNovelType(typeName);
    }

    @Override
    public List<Type> showTypes() {
        return novelMapper.selectAllTypes();
    }

    @Override
    public List<Novel> selectNovel(SearchInfo searchInfo) {
        PageHelper.startPage(searchInfo.getPage(), searchInfo.getPageSize());
        return novelMapper.selectNovel(searchInfo);
    }

    @Override
    public List<Novel> showPopular() {
        return novelMapper.selectPopular();
    }

    @Override
    public void addTimes(String filePath) {
        novelMapper.addTimes(filePath);
    }

}
