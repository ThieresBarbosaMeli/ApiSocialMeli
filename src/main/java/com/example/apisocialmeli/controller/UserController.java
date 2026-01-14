package com.example.apisocialmeli.controller;

import com.example.apisocialmeli.dto.request.CreateUserRequest;
import com.example.apisocialmeli.domain.User;
import com.example.apisocialmeli.dto.response.FollowedDTO;
import com.example.apisocialmeli.dto.response.FollowedListResponseDTO;
import com.example.apisocialmeli.dto.response.FollowerDTO;
import com.example.apisocialmeli.dto.response.FollowersCountResponseDTO;
import com.example.apisocialmeli.dto.response.FollowersListResponseDTO;
import com.example.apisocialmeli.dto.request.UpdatePasswordRequest;
import com.example.apisocialmeli.dto.request.UpdateUserProfileRequest;
import com.example.apisocialmeli.service.UserService;
import com.example.apisocialmeli.mapper.UserMapper;
import com.example.apisocialmeli.exception.ErrorMessages;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<Void> registerUser(@Valid @RequestBody CreateUserRequest request) {
        userService.register(
                request.getId(),
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllData() {
        userService.deleteAllData();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateProfile(@PathVariable @Positive(message = ErrorMessages.USER_ID_POSITIVE_PATH) int userId,
                                              @Valid @RequestBody UpdateUserProfileRequest request) {
        userService.updateProfile(userId, request.getName(), request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable @Positive(message = ErrorMessages.USER_ID_POSITIVE_PATH) int userId,
                                               @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(userId, request.getPassword());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive(message = ErrorMessages.USER_ID_POSITIVE_PATH) int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public ResponseEntity<Void> followUser(
            @PathVariable @Positive(message = ErrorMessages.USER_ID_POSITIVE_PATH) int userId,
            @PathVariable @Positive(message = ErrorMessages.USER_ID_TO_FOLLOW_POSITIVE) int userIdToFollow) {
        userService.follow(userId, userIdToFollow);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/unfollow/{userIdToUnfollow}")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable @Positive(message = ErrorMessages.USER_ID_POSITIVE_PATH) int userId,
            @PathVariable @Positive(message = ErrorMessages.USER_ID_TO_UNFOLLOW_POSITIVE) int userIdToUnfollow) {
        userService.unfollow(userId, userIdToUnfollow);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<FollowersCountResponseDTO> getFollowersCount(
            @PathVariable @Positive(message = ErrorMessages.USER_ID_POSITIVE_PATH) int userId) {
        User user = userService.getUserById(userId);
        int count = user.getFollowers().size();

        FollowersCountResponseDTO response =
                new FollowersCountResponseDTO(user.getId(), user.getName(), count);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/followers/list")
    public ResponseEntity<FollowersListResponseDTO> getFollowers(
            @PathVariable @Positive(message = ErrorMessages.USER_ID_POSITIVE_PATH) int userId,
            @RequestParam(required = false, defaultValue = "") String order) {
        validateNameOrder(order);

        User user = userService.getUserById(userId);
        Set<Integer> followerIds = userService.getFollowers(userId);

        Collection<User> followerUsers = followerIds.stream()
                .map(userService::getUserById)
                .toList();

        List<FollowerDTO> followers = UserMapper.toFollowerDTOList(followerUsers, order);

        FollowersListResponseDTO response = new FollowersListResponseDTO(
                user.getId(),
                user.getName(),
                followers
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/followed/list")
    public ResponseEntity<FollowedListResponseDTO> getFollowing(
            @PathVariable @Positive(message = ErrorMessages.USER_ID_POSITIVE_PATH) int userId,
            @RequestParam(required = false, defaultValue = "") String order) {
        validateNameOrder(order);

        User user = userService.getUserById(userId);
        Set<Integer> followingIds = userService.getFollowing(userId);

        Collection<User> followedUsers = followingIds.stream()
                .map(userService::getUserById)
                .toList();

        List<FollowedDTO> followed = UserMapper.toFollowedDTOList(followedUsers, order);

        FollowedListResponseDTO response = new FollowedListResponseDTO(
                user.getId(),
                user.getName(),
                followed
        );

        return ResponseEntity.ok(response);
    }

    private void validateNameOrder(String order) {
        if (order.isEmpty()) return;

        if (!order.equalsIgnoreCase("name_asc") && !order.equalsIgnoreCase("name_desc")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    ErrorMessages.INVALID_NAME_ORDER);
        }
    }
}