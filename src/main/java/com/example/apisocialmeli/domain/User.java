package com.example.apisocialmeli.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    private int id;

    @Column(length = 40, nullable = false)
    private String name;

    @Column(length = 60, nullable = false)
    private String email;

    @Column(length = 60)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_following", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "following_id")
    private Set<Integer> following = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_followers", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "follower_id")
    private Set<Integer> followers = new HashSet<>();

    protected User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(int id, String name, String email) {
        this(id, name, email, null);
    }

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
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

    public Set<Integer> getFollowers() {
        return Collections.unmodifiableSet(followers);
    }

    public Set<Integer> getFollowing() {
        return Collections.unmodifiableSet(following);
    }

    public void addFollowing(int userId) {
        following.add(userId);
    }

    public boolean isFollowing(int userId) {
        return following.contains(userId);
    }

    public void removeFollowing(int userIdUnfollow) {
        following.remove(userIdUnfollow);
    }

    public void addFollower(int followerId) {
        followers.add(followerId);
    }

    public boolean hasFollower(int followerId) {
        return followers.contains(followerId);
    }

    public void removeFollower(int followerId) {
        followers.remove(followerId);
    }

    Set<Integer> getFollowingInternal() {
        return following;
    }

    Set<Integer> getFollowersInternal() {
        return followers;
    }
}