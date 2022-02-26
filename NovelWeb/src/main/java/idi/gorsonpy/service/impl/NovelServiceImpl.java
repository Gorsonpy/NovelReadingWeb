package idi.gorsonpy.service.impl;


import com.github.pagehelper.PageHelper;
import idi.gorsonpy.domain.Novel;
import idi.gorsonpy.mapper.NovelMapper;
import idi.gorsonpy.service.NovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("novelService")
public class NovelServiceImpl implements NovelService {
    @Autowired
    NovelMapper novelMapper;

    @Override
    public void saveNovel(String name, String author, String description, String picturePath, String txtPath) {
        novelMapper.addNovel(name, author, description, picturePath, txtPath);
    }

    @Override
    public List<Novel> showByPage(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Novel> novels = novelMapper.selectAll();
        return novels;
    }
}
