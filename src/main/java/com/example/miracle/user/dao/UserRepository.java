package com.example.miracle.user.dao;

import com.example.miracle.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(" select u from User u " +
            " where lower(u.username) like lower(concat('%', :text, '%')) ")
    List<User> search(@Param("text") String text, Pageable pageable);

    Optional<User> findByEmail(String email);

    Optional<User> findByActivationCode(String code);

    Optional<User> findByLogin(String login);
}
