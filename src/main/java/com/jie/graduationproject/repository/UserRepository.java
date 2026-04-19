package com.jie.graduationproject.repository;

import com.jie.graduationproject.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    void deleteByUsername(String username);

    Optional<User> findUserByUsername(String username);

    boolean existsUserById(Long id);

    boolean existsUserByUsername(String username);
    
    long countByRole(String role);

}
