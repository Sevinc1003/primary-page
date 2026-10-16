package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.GetUsersResponse;
import com.example.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // @Query("SELECT u.password FROM User u WHERE u.email = :email")
    // Optional<String> findPasswordByEmail(@Param("email") String email);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = "SELECT new com.example.demo.dto.GetUsersResponse(u.name, u.surname, u.email, u.registerDate, u.lastLoginDate) "
            +
            "FROM User u WHERE u.role = 'USER'", countQuery = "SELECT COUNT(u) FROM User u WHERE u.role = 'USER'")
    Page<GetUsersResponse> getUsers(Pageable pageable);
}
