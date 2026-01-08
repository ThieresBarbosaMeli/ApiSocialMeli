package com.example.apisocialmeli;

import java.util.HashSet;
import java.util.Set;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private Set<Integer> following;
    private Set<Integer> followers;

    public User(int id, String name, String email) {
        this(id, name, email, null);
    }

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.following = new HashSet<>();
        this.followers = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Integer> getFollowing() {
        return following;
    }

    public Set<Integer> getFollowers() {
        return followers;
    }

    public void addFollowing(int userId) {
        following.add(userId);
    }

    public void removeFollowing(int userIdUnfollow) {
        following.remove(userIdUnfollow);
    }

    public void addFollower(int followerId) {
        followers.add(followerId);
    }

    public void removeFollower(int followerId) {
        followers.remove(followerId);
    }
}