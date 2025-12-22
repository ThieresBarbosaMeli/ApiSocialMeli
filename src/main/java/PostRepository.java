import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class PostRepository {
    private Map<Integer, Post> posts = new HashMap<>();

    public void save(Post post) {
        posts.put(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }
}