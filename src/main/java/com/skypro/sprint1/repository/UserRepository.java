package com.skypro.sprint1.repository;

import com.skypro.sprint1.model.FullName;
import com.skypro.sprint1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с пользователями.
 *
 * @author Nikita Malinkin
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Возвращает идентификатор пользователя по его имени.
     *
     * @param userName Имя пользователя.
     * @return Идентификатор пользователя.
     */
    @Query(
            nativeQuery = true,
            value = "SELECT id FROM users " +
                    "WHERE user_name = :userName"
    )
    UUID findIdByUserName(String userName);

    /**
     * Возвращает полное имя пользователя по его имени.
     *
     * @param userName Имя пользователя.
     * @return Полное имя пользователя.
     */
    @Query(
            nativeQuery = true,
            value = "SELECT first_name AS firstName, last_name AS lastName FROM users " +
                    "WHERE user_name = :userName"
    )
    FullName findFullNameByUserName(String userName);
}
