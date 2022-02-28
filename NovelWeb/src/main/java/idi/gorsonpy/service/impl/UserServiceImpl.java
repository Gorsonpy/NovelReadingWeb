package idi.gorsonpy.service.impl;

import com.github.pagehelper.PageHelper;
import idi.gorsonpy.domain.Favorites;
import idi.gorsonpy.domain.Novel;
import idi.gorsonpy.domain.User;
import idi.gorsonpy.mapper.NovelMapper;
import idi.gorsonpy.mapper.UserMapper;
import idi.gorsonpy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NovelMapper novelMapper;

    public User Login(String username, String password) {
        return userMapper.findUserByUsernameAndPassword(username, password);
    }

    @Override
    public boolean UserNameIsUsed(String username) {
        User user = userMapper.selectUserByUsername(username);
        return user != null;
    }

    @Override
    public void register(String username, String password, boolean isAdmin) {
        userMapper.addUser(username, password, isAdmin);
    }

    @Override
    public void collect(Long userId, Long novelId) {
        userMapper.addUserNovelRel(userId, novelId);
    }


    @Override
    public boolean IsCollect(Long userId, Long novelId) {
        Favorites favorites = userMapper.selectFavoritesRel(userId, novelId);
        return favorites != null;
    }

    @Override
    public void delCollect(Long userId, Long novelId) {
        userMapper.delUserNovelRel(userId, novelId);
    }

    public List<Novel> selectUserCollect(Long userId) {
        return userMapper.selectUserCollect(userId);
    }

    @Override
    public List<Novel> showNovels(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        return novelMapper.selectUnchecked();
    }

    @Override
    public void checkNovels(Long novelId, String checkStatus) {
        // checkStatus仅当为"1"时认为审核通过
        if ("1".equals(checkStatus)) {
            novelMapper.updateCheckedStatus(novelId);
        } else {
            novelMapper.delNovel(novelId);
        }
    }
}
