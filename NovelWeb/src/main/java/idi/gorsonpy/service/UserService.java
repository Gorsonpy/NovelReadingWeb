package idi.gorsonpy.service;

import idi.gorsonpy.domain.User;

public interface UserService {
    User Login(String username, String password);
}
