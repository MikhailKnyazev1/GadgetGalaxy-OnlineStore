package com.example.gadgetgalaxy.rdb;

import com.example.gadgetgalaxy.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("userCustomRepository")
public class UserCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> findAll() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }

    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    public User findByUsername(String username) {
        List<User> users = entityManager.createQuery("from User where username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        if (users != null && !users.isEmpty()) {
            return users.get(0);
        }
        return null;  // или выбросите ваше собственное исключение, если это необходимо
    }


    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    public void deleteById(Long id) {
        User user = findById(id);
        if (user != null) {
            entityManager.remove(user);
        }
    }
}
