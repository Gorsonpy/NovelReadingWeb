package idi.gorsonpy.service.impl;

import idi.gorsonpy.domain.User;
import idi.gorsonpy.mapper.UserMapper;
import idi.gorsonpy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    public User Login(String username, String password) {
        return userMapper.findUserByUsernameAndPassword(username, password);
    }
}
