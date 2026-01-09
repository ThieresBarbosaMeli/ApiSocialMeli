package com.example.apisocialmeli.repository;

import com.example.apisocialmeli.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public User findById(int id) {
        return users.get(id);
    }

    @Override
    public boolean existsById(int id) {
        return users.containsKey(id);
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void delete(int id) {
        users.remove(id);
    }
}

