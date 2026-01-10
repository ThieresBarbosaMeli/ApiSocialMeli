package com.example.apisocialmeli;

import com.example.apisocialmeli.domain.User;
import com.example.apisocialmeli.repository.InMemoryUserRepository;
import com.example.apisocialmeli.repository.UserRepository;
import com.example.apisocialmeli.service.UserService;
import com.example.apisocialmeli.service.impl.UserServiceImpl;
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
        userRepository = new InMemoryUserRepository();
        userService = new UserServiceImpl(userRepository);
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

        User updated = userRepository.findById(1).orElseThrow();
        assertEquals("Alice Nova", updated.getName());
        assertEquals("alice.nova@test.com", updated.getEmail());
    }

    @Test
    void updatePasswordShouldChangePassword() {
        userRepository.save(new User(1, "Alice", "alice@test.com", "123456"));

        userService.updatePassword(1, "novaSenha!");

        User updated = userRepository.findById(1).orElseThrow();
        assertEquals("novaSenha!", updated.getPassword());
    }

    @Test
    void registerShouldRejectNonPositiveId() {
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> userService.register(0, "Nome", "email@test.com", "123456")
        );
        assertEquals("O id do usuário deve ser maior que zero.", ex.getReason());
    }

    @Test
    void registerShouldRejectDuplicateId() {
        userService.register(1, "Alice", "alice@test.com", "123456");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> userService.register(1, "Bob", "bob@test.com", "123456")
        );
        assertEquals("Já existe um usuário com o id 1", ex.getReason());
    }

    @Test
    void whenUserTriesToFollowItself_shouldThrowException() {
        userRepository.save(new User(1, "Alice", "alice@test.com"));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> userService.follow(1, 1)
        );
        assertEquals("Um usuário não pode seguir a si mesmo.", ex.getReason());
    }

    @Test
    void whenUserTriesToUnfollowItself_shouldThrowException() {
        userRepository.save(new User(1, "Alice", "alice@test.com"));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> userService.unfollow(1, 1)
        );
        assertEquals("Um usuário não pode deixar de seguir a si mesmo.", ex.getReason());
    }

    @Test
    void shouldOrderFollowersByNameAscending_T0004() {
        User seller = new User(1, "Vendedor", "seller@test.com");
        userRepository.save(seller);
        userRepository.save(new User(2, "Charlie", "charlie@test.com"));
        userRepository.save(new User(3, "Alice", "alice@test.com"));
        userRepository.save(new User(4, "Bob", "bob@test.com"));

        userService.follow(2, 1);
        userService.follow(3, 1);
        userService.follow(4, 1);

        var followers = userService.getFollowers(1).stream()
                .map(userService::getUserById)
                .sorted((u1, u2) -> u1.getName().compareToIgnoreCase(u2.getName()))
                .toList();

        assertEquals("Alice", followers.get(0).getName());
        assertEquals("Bob", followers.get(1).getName());
        assertEquals("Charlie", followers.get(2).getName());
    }

    @Test
    void shouldOrderFollowersByNameDescending_T0004() {
        User seller = new User(1, "Vendedor", "seller@test.com");
        userRepository.save(seller);
        userRepository.save(new User(2, "Charlie", "charlie@test.com"));
        userRepository.save(new User(3, "Alice", "alice@test.com"));
        userRepository.save(new User(4, "Bob", "bob@test.com"));

        userService.follow(2, 1);
        userService.follow(3, 1);
        userService.follow(4, 1);

        var followers = userService.getFollowers(1).stream()
                .map(userService::getUserById)
                .sorted((u1, u2) -> u2.getName().compareToIgnoreCase(u1.getName()))
                .toList();

        assertEquals("Charlie", followers.get(0).getName());
        assertEquals("Bob", followers.get(1).getName());
        assertEquals("Alice", followers.get(2).getName());
    }
}