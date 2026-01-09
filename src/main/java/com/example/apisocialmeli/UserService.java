package com.example.apisocialmeli;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(int id, String name, String email, String password) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O id do usuário deve ser maior que zero.");
        }

        if (userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Já existe um usuário com o id " + id);
        }
        User user = new User(id, name, email, password);
        userRepository.save(user);
    }

    public void updateProfile(int userId, String name, String email) {
        User user = getUserById(userId);
        user.setName(name);
        user.setEmail(email);
        userRepository.save(user);
    }

    public void updatePassword(int userId, String newPassword) {
        User user = getUserById(userId);
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public void deleteUser(int userId) {
        User user = getUserById(userId);
        userRepository.findAll().forEach(otherUser -> {
            otherUser.getFollowers().remove(userId);
            otherUser.getFollowing().remove(userId);
        });
        userRepository.delete(userId);
    }

    public void follow(int userId, int userIdToFollow) {
        if (userId == userIdToFollow) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Um usuário não pode seguir a si mesmo.");
        }

        User user = getUserById(userId);
        User userToFollow = getUserById(userIdToFollow);

        if (user.getFollowing().contains(userIdToFollow)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O usuário " + userId + " já segue " + userIdToFollow + ".");
        }

        user.addFollowing(userIdToFollow);
        userToFollow.addFollower(userId);
    }

    public void unfollow(int userId, int userIdToUnfollow) {
        if (userId == userIdToUnfollow) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Um usuário não pode deixar de seguir a si mesmo.");
        }

        User user = getUserById(userId);
        User userToUnfollow = getUserById(userIdToUnfollow);

        if (!user.getFollowing().contains(userIdToUnfollow)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O usuário " + userId + " não segue " + userIdToUnfollow + ".");
        }

        user.removeFollowing(userIdToUnfollow);
        userToUnfollow.removeFollower(userId);
    }

    public int getFollowerCount(int userId) {
        return getUserById(userId).getFollowers().size();
    }

    public Set<Integer> getFollowers(int userId) {
        return getUserById(userId).getFollowers();
    }

    public Set<Integer> getFollowing(int userId) {
        return getUserById(userId).getFollowing();
    }

    public User getUserById(int userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Usuário não encontrado: " + userId);
        }
        return user;
    }
}