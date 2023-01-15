package de.holarse.backend.db.repositories;

import de.holarse.backend.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByLogin(final String login);
    
}
