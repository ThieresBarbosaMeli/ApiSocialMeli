package com.example.apisocialmeli.service.impl;

import com.example.apisocialmeli.domain.User;
import com.example.apisocialmeli.exception.ErrorMessages;
import com.example.apisocialmeli.repository.PostRepository;
import com.example.apisocialmeli.repository.UserRepository;
import com.example.apisocialmeli.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserServiceImpl(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void register(int id, String name, String email, String password) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O id do usuário deve ser maior que zero.");
        }

        if (userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Já existe um usuário com o id %d", id));
        }
        User user = new User(id, name, email, password);
        userRepository.save(user);
    }

    @Override
    public void updateProfile(int userId, String name, String email) {
        User user = getUserById(userId);
        user.setName(name);
        user.setEmail(email);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(int userId, String newPassword) {
        User user = getUserById(userId);
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(int userId) {
        getUserById(userId);
        userRepository.findAll().forEach(otherUser -> {
            otherUser.removeFollower(userId);
            otherUser.removeFollowing(userId);
            userRepository.save(otherUser);
        });
        userRepository.deleteById(userId);
    }

    @Override
    public void follow(int userId, int userIdToFollow) {
        if (userId == userIdToFollow) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Um usuário não pode seguir a si mesmo.");
        }

        User user = getUserById(userId);
        User userToFollow = getUserById(userIdToFollow);

        if (user.isFollowing(userIdToFollow)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("O usuário %d já segue %d.", userId, userIdToFollow));
        }

        user.addFollowing(userIdToFollow);
        userToFollow.addFollower(userId);
        userRepository.save(user);
        userRepository.save(userToFollow);
    }

    @Override
    public void unfollow(int userId, int userIdToUnfollow) {
        if (userId == userIdToUnfollow) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Um usuário não pode deixar de seguir a si mesmo.");
        }

        User user = getUserById(userId);
        User userToUnfollow = getUserById(userIdToUnfollow);

        if (!user.isFollowing(userIdToUnfollow)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("O usuário %d não segue %d.", userId, userIdToUnfollow));
        }

        user.removeFollowing(userIdToUnfollow);
        userToUnfollow.removeFollower(userId);
        userRepository.save(user);
        userRepository.save(userToUnfollow);
    }

    @Override
    public int getFollowerCount(int userId) {
        return getUserById(userId).getFollowers().size();
    }

    @Override
    public Set<Integer> getFollowers(int userId) {
        return getUserById(userId).getFollowers();
    }

    @Override
    public Set<Integer> getFollowing(int userId) {
        return getUserById(userId).getFollowing();
    }

    @Override
    public User getUserById(int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Usuário não encontrado: %d", userId));
        }
        return user;
    }

    @Override
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteAllData() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }
}
