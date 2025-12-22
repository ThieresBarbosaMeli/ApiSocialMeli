public class App {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);

        userService.register(1, "Alice", "alice@example.com");
        userService.register(2, "Bob", "thierry@example.com");
        userService.register(3, "Carol", "thiago@example.com");

        userService.follow(1, 2);
        userService.follow(1, 3);
        userService.follow(2, 3);

        System.out.println("Alice followers count: " + userService.getFollowerCount(1));
        System.out.println("Alice is following: " + userService.getFollowing(1));
        System.out.println("Alice followers: " + userService.getFollowers(1));
    }
}