package idi.gorsonpy.service;

import idi.gorsonpy.domain.User;

public interface UserService {
    User Login(String username, String password);

    boolean checkUserUsed(String username);

    void register(String username, String password);
}
