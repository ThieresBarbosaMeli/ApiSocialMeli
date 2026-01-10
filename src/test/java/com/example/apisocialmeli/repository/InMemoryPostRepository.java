package com.example.apisocialmeli.repository;

import com.example.apisocialmeli.domain.Post;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class InMemoryPostRepository implements PostRepository {

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public Post save(Post post) {
        if (post.getId() == 0) {
            int newId = idGenerator.getAndIncrement();
            try {
                var idField = Post.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(post, newId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to set post ID", e);
            }
        }
        posts.put(post.getId(), post);
        return post;
    }

    @Override
    public Optional<Post> findById(Integer id) {
        return Optional.ofNullable(posts.get(id));
    }

    @Override
    public boolean existsById(Integer id) {
        return posts.containsKey(id);
    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }

    @Override
    public void deleteById(Integer id) {
        posts.remove(id);
    }

    @Override
    public void delete(Post post) {
        posts.remove(post.getId());
    }

    @Override
    public long count() {
        return posts.size();
    }

    @Override
    public void flush() {
    }

    @Override
    public <S extends Post> S saveAndFlush(S entity) {
        posts.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends Post> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach(e -> {
            save(e);
            result.add(e);
        });
        return result;
    }

    @Override
    public void deleteAll(Iterable<? extends Post> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        posts.clear();
    }

    @Override
    public <S extends Post> List<S> saveAllAndFlush(Iterable<S> entities) {
        return saveAll(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<Post> entities) {
        deleteAll(entities);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {
        integers.forEach(posts::remove);
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {
        integers.forEach(posts::remove);
    }

    @Override
    public void deleteAllInBatch() {
        posts.clear();
    }

    @Override
    public Post getOne(Integer integer) {
        return posts.get(integer);
    }

    @Override
    public Post getById(Integer integer) {
        return posts.get(integer);
    }

    @Override
    public Post getReferenceById(Integer integer) {
        return posts.get(integer);
    }

    @Override
    public List<Post> findAllById(Iterable<Integer> integers) {
        List<Post> result = new ArrayList<>();
        integers.forEach(id -> {
            Post post = posts.get(id);
            if (post != null) result.add(post);
        });
        return result;
    }

    @Override
    public List<Post> findAll(Sort sort) {
        return findAll();
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Post> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Post> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Post> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Post> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Post> long count(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Post> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Post, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException();
    }
}
