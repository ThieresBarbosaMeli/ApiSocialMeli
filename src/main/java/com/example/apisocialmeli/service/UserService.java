package com.example.apisocialmeli.service;

import com.example.apisocialmeli.domain.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    void register(int id, String name, String email, String password);

    void updateProfile(int userId, String name, String email);

    void updatePassword(int userId, String newPassword);

    void deleteUser(int userId);

    void follow(int userId, int userIdToFollow);

    void unfollow(int userId, int userIdToUnfollow);

    int getFollowerCount(int userId);

    Set<Integer> getFollowers(int userId);

    Set<Integer> getFollowing(int userId);

    User getUserById(int userId);

    List<User> getAllUsers();

    void deleteAllData();
}

