package com.skypro.sprint1.repository;

import com.skypro.sprint1.model.FullName;
import com.skypro.sprint1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(
            nativeQuery = true,
            value = "SELECT id FROM users " +
                    "WHERE user_name = :userName"
    )
    UUID findIdByUserName(String userName);

    @Query(
            nativeQuery = true,
            value = "SELECT first_name AS firstName, last_name AS lastName FROM users " +
                    "WHERE user_name = :userName"
    )
    FullName findFullNameByUserName(String userName);
}
