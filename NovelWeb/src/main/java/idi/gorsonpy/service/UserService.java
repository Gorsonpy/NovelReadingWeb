package idi.gorsonpy.service;

import idi.gorsonpy.domain.User;

public interface UserService {
    User Login(String username, String password);

    boolean UserNameIsUsed(String username);

    void register(String username, String password, boolean isAdmin);
}
