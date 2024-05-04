package com.example.cudanawidelcu.services.recipes.repository;

import com.example.cudanawidelcu.services.recipes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findUserByLogin(String username);
    List<User> findUserByPasswordIsNull();

    @Query("select u from User u where u.login LIKE :nlike")
    List<User> findByQueryWithLike(@Param("nlike") String nlike);
}
