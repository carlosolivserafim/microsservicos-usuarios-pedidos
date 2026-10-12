package io.github.carlosolivserafim.user.api.repository;

import io.github.carlosolivserafim.user.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}