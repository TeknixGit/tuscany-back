package com.tuscany.tour.repository;

import com.tuscany.tour.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    Boolean existsByUserName(String username);


    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}


