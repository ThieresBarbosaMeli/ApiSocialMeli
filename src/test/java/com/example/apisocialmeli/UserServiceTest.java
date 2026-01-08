package com.example.apisocialmeli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        userService = new UserService(userRepository);
    }

    @Test
    void whenFollowerDoesNotExist_shouldThrowException() {
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> userService.follow(1, 2)
        );
        assertEquals("Usuário não encontrado: 1", ex.getReason());
    }

    @Test
    void whenUserToFollowDoesNotExist_shouldThrowException() {
        userRepository.save(new User(1, "Alice", "alice@test.com"));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> userService.follow(1, 2)
        );
        assertEquals("Usuário não encontrado: 2", ex.getReason());
    }

    @Test
    void whenUserAlreadyFollows_shouldThrowException() {
        userRepository.save(new User(1, "Alice", "alice@test.com"));
        userRepository.save(new User(2, "Bob", "bob@test.com"));

        userService.follow(1, 2);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> userService.follow(1, 2)
        );

        assertEquals("O usuário 1 já segue 2.", ex.getReason());
    }

    @Test
    void followerCountShouldReturnCorrectNumber() {
        User user = new User(1, "Alice", "alice@test.com");
        user.addFollower(10);
        user.addFollower(11);
        userRepository.save(user);

        int count = userService.getFollowerCount(1);

        assertEquals(2, count);
    }

    @Test
    void whenUnfollowWithoutFollowing_shouldThrowException() {
        userRepository.save(new User(1, "Alice", "alice@test.com"));
        userRepository.save(new User(2, "Bob", "bob@test.com"));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> userService.unfollow(1, 2)
        );

        assertEquals("O usuário 1 não segue 2.", ex.getReason());
    }

    @Test
    void updateProfileShouldChangeNameAndEmail() {
        userRepository.save(new User(1, "Alice", "alice@test.com"));

        userService.updateProfile(1, "Alice Nova", "alice.nova@test.com");

        User updated = userRepository.findById(1);
        assertEquals("Alice Nova", updated.getName());
        assertEquals("alice.nova@test.com", updated.getEmail());
    }

    @Test
    void updatePasswordShouldChangePassword() {
        userRepository.save(new User(1, "Alice", "alice@test.com", "123456"));

        userService.updatePassword(1, "novaSenha!");

        User updated = userRepository.findById(1);
        assertEquals("novaSenha!", updated.getPassword());
    }
}