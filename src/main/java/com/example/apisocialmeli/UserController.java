package com.example.apisocialmeli;

import com.example.apisocialmeli.dto.FollowedDTO;
import com.example.apisocialmeli.dto.FollowedListResponseDTO;
import com.example.apisocialmeli.dto.FollowerDTO;
import com.example.apisocialmeli.dto.FollowersCountResponseDTO;
import com.example.apisocialmeli.dto.FollowersListResponseDTO;
import com.example.apisocialmeli.dto.UpdatePasswordRequest;
import com.example.apisocialmeli.dto.UpdateUserProfileRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody CreateUserRequest request) {
        userService.register(
                request.getId(),
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateProfile(@PathVariable int userId,
                                              @Valid @RequestBody UpdateUserProfileRequest request) {
        userService.updateProfile(userId, request.getName(), request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable int userId,
                                               @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(userId, request.getPassword());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<FollowersCountResponseDTO> getFollowersCount(@PathVariable int userId) {
        User user = userService.getUserById(userId);
        int count = user.getFollowers().size();

        FollowersCountResponseDTO response =
                new FollowersCountResponseDTO(user.getId(), user.getName(), count);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/followers/list")
    public ResponseEntity<FollowersListResponseDTO> getFollowers(@PathVariable int userId,
                                                                 @RequestParam(required = false, defaultValue = "") String order) {
        validateNameOrder(order);

        User user = userService.getUserById(userId);
        Set<Integer> followerIds = user.getFollowers();

        List<FollowerDTO> followers = followerIds.stream()
                .map(userService::getUserById)
                .map(follower -> new FollowerDTO(follower.getId(), follower.getName()))
                .toList();

        followers = sortFollowersByName(followers, order);

        FollowersListResponseDTO response = new FollowersListResponseDTO(
                user.getId(),
                user.getName(),
                followers
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/followed/list")
    public ResponseEntity<FollowedListResponseDTO> getFollowing(@PathVariable int userId,
                                                                @RequestParam(required = false, defaultValue = "") String order) {
        validateNameOrder(order);

        User user = userService.getUserById(userId);
        Set<Integer> followingIds = user.getFollowing();

        List<FollowedDTO> followed = followingIds.stream()
                .map(userService::getUserById)
                .map(followedUser -> new FollowedDTO(followedUser.getId(), followedUser.getName()))
                .toList();

        followed = sortFollowedByName(followed, order);

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
                    "Parâmetro 'order' inválido. Use name_asc ou name_desc.");
        }
    }

    private List<FollowerDTO> sortFollowersByName(List<FollowerDTO> followers, String order) {
        if (order.isEmpty()) return followers;

        return followers.stream()
                .sorted(order.equalsIgnoreCase("name_asc")
                        ? Comparator.comparing(FollowerDTO::getUserName)
                        : Comparator.comparing(FollowerDTO::getUserName).reversed())
                .toList();
    }

    private List<FollowedDTO> sortFollowedByName(List<FollowedDTO> followed, String order) {
        if (order.isEmpty()) return followed;

        return followed.stream()
                .sorted(order.equalsIgnoreCase("name_asc")
                        ? Comparator.comparing(FollowedDTO::getUserName)
                        : Comparator.comparing(FollowedDTO::getUserName).reversed())
                .toList();
    }
}