package com.example.apisocialmeli;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody CreateUserRequest request) {
        userService.register(request.getId(), request.getName(), request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public ResponseEntity<Void> followUser(@PathVariable int userId,
                                           @PathVariable int userIdToFollow) {
        userService.follow(userId, userIdToFollow);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/unfollow/{userIdToUnfollow}")
    public ResponseEntity<Void> unfollowUser(@PathVariable int userId,
                                             @PathVariable int userIdToUnfollow) {
        userService.unfollow(userId, userIdToUnfollow);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Integer> getFollowersCount(@PathVariable int userId) {
        int count = userService.getFollowerCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{userId}/followers/list")
    public ResponseEntity<List<UserSummaryDTO>> getFollowers(@PathVariable int userId) {
        Set<Integer> followerIds = userService.getFollowers(userId);
        List<UserSummaryDTO> body = followerIds.stream()
                .map(UserSummaryDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{userId}/following/list")
    public ResponseEntity<List<UserSummaryDTO>> getFollowing(@PathVariable int userId) {
        Set<Integer> followingIds = userService.getFollowing(userId);
        List<UserSummaryDTO> body = followingIds.stream()
                .map(UserSummaryDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}