import java.util.Set;
import java.util.Collections;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(int id, String name, String email) {
        User user = new User(id, name, email);
        userRepository.save(user);
    }

    public void follow(int userId, int userIdToFollow) {
        User user = userRepository.findById(userId);
        User userToFollow = userRepository.findById(userIdToFollow);

        if (user != null && userToFollow != null) {
            user.addFollowing(userIdToFollow);
            userToFollow.addFollower(userId);
        }
    }

    public void unfollow(int userId, int userIdToUnfollow) {
        User user = userRepository.findById(userId);
        User userToUnfollow = userRepository.findById(userIdToUnfollow);

        if (user != null && userToUnfollow != null) {
            user.removeFollowing(userIdToUnfollow);
            userToUnfollow.removeFollower(userId);
        }
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