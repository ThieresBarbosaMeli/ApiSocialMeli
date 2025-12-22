import java.util.HashSet;
import java.util.Set;

public class User {
    private int id;
    private String name;
    private String email;
    private Set<Integer> following; // IDs de usuários que este user segue
    private Set<Integer> followers; // IDs de usuários que seguem este user

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.following = new HashSet<>();
        this.followers = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Set<Integer> getFollowing() {
        return following;
    }

    public Set<Integer> getFollowers() {
        return followers;
    }

    public void addFollowing(int userId) {
        following.add(userId);
    }

    public void removeFollowing(int userIdUnfollow) {
        following.remove(userIdUnfollow);

    }
    public void addFollower(int followerId) {
        followers.add(followerId);
    }

    public void removeFollower(int followerId) {
        followers.remove(followerId);
    }
}


