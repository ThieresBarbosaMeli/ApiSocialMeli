package com.example.apisocialmeli;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.Collections;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(int id, String name, String email) {
        User user = new User(id, name, email);
        userRepository.save(user);
    }

    public void follow(int userId, int userIdToFollow) {
        if (userId == userIdToFollow) {
            throw new IllegalArgumentException("Um usuário não pode seguir a si mesmo.");
        }

        User user = userRepository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("Usuário seguidor não encontrado: " + userId);
        }

        User userToFollow = userRepository.findById(userIdToFollow);
        if (userToFollow == null) {
            throw new UserNotFoundException("Usuário a ser seguido não encontrado: " + userIdToFollow);
        }

        user.addFollowing(userIdToFollow);
        userToFollow.addFollower(userId);
    }

    public void unfollow(int userId, int userIdToUnfollow) {
        if (userId == userIdToUnfollow) {
            throw new IllegalArgumentException("Um usuário não pode deixar de seguir a si mesmo.");
        }

        User user = userRepository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("Usuário seguidor não encontrado: " + userId);
        }

        User userToUnfollow = userRepository.findById(userIdToUnfollow);
        if (userToUnfollow == null) {
            throw new UserNotFoundException("Usuário a ser deixado de seguir não encontrado: " + userIdToUnfollow);
        }

        user.removeFollowing(userIdToUnfollow);
        userToUnfollow.removeFollower(userId);
    }

    public int getFollowerCount(int userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            return user.getFollowers().size();
        }
        return 0;
    }

    public Set<Integer> getFollowers(int userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            return user.getFollowers();
        }
        return Collections.emptySet();
    }

    public Set<Integer> getFollowing(int userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            return user.getFollowing();
        }
        return Collections.emptySet();
    }
}