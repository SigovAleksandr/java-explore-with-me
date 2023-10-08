package ru.practicum.ewm.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.service.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
