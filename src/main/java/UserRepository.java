import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private Map<Integer, User> users = new HashMap<>();

    public void save(User user) {
        users.put(user.getId(), user);
    }
    public User findById(int id) {
        return users.get(id);
    }
    public boolean existsById(int id) {
        return users.containsKey(id);
    }

}
