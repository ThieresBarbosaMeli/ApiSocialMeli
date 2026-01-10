package com.example.apisocialmeli.repository;

import com.example.apisocialmeli.domain.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class InMemoryUserRepository implements UserRepository {

    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean existsById(Integer id) {
        return users.containsKey(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(Integer id) {
        users.remove(id);
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public long count() {
        return users.size();
    }

    @Override
    public void flush() {
    }

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        users.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach(e -> {
            users.put(e.getId(), e);
            result.add(e);
        });
        return result;
    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return saveAll(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {
        deleteAll(entities);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {
        integers.forEach(users::remove);
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {
        integers.forEach(users::remove);
    }

    @Override
    public void deleteAllInBatch() {
        users.clear();
    }

    @Override
    public User getOne(Integer integer) {
        return users.get(integer);
    }

    @Override
    public User getById(Integer integer) {
        return users.get(integer);
    }

    @Override
    public User getReferenceById(Integer integer) {
        return users.get(integer);
    }

    @Override
    public List<User> findAllById(Iterable<Integer> integers) {
        List<User> result = new ArrayList<>();
        integers.forEach(id -> {
            User user = users.get(id);
            if (user != null) result.add(user);
        });
        return result;
    }

    @Override
    public List<User> findAll(Sort sort) {
        return findAll();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException();
    }
}
