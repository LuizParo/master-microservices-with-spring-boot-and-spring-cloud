package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import org.springframework.stereotype.Repository;

@Repository
public class UserInMemoryDao {
    private static final List<User> USERS = new ArrayList<>();
    private static final AtomicLong USERS_COUNT = new AtomicLong();

    static {
        USERS.add(new User(1L, "Adam", new Date()));
        USERS.add(new User(2L, "Eve", new Date()));
        USERS.add(new User(3L, "Jack", new Date()));

        USERS_COUNT.set(USERS.size());
    }

    public Iterable<User> findAll() {
        return new ArrayList<>(USERS);
    }

    public Optional<User> findOne(Long id) {
        return USERS.stream()
                .filter(user -> user.getId().isPresent() ? user.getId().get() == id : false)
                .findFirst();
    }

    public User save(User user) {
        Long id = user.getId().orElseGet(() -> USERS_COUNT.incrementAndGet());
        user.setId(id);

        USERS.add(user);
        return user;
    }

    public Optional<User> remove(Long id) {
        Optional<User> foundUser = this.findOne(id);
        foundUser.ifPresent(USERS::remove);

        return foundUser;
    }
}