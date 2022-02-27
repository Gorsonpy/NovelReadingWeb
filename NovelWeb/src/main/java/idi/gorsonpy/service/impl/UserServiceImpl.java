package idi.gorsonpy.service.impl;

import idi.gorsonpy.domain.Favorites;
import idi.gorsonpy.domain.Novel;
import idi.gorsonpy.domain.User;
import idi.gorsonpy.mapper.UserMapper;
import idi.gorsonpy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
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
        userMapper.selectUserCollect(userId);
    }
}
