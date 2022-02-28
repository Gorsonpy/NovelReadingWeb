package idi.gorsonpy.service;

import idi.gorsonpy.domain.Novel;
import idi.gorsonpy.domain.User;

import java.util.List;

public interface UserService {
    User Login(String username, String password);

    boolean UserNameIsUsed(String username);

    void register(String username, String password, boolean isAdmin);

    void collect(Long userId, Long novelId);

    boolean IsCollect(Long userId, Long novelId);

    void delCollect(Long userId, Long novelId);

    List<Novel> selectUserCollect(Long userId);

    List<Novel> showNovels(Integer page, Integer pageSize);

    void checkNovels(Long novelId, String checkStatus);
}
