package juro.copyjuro.repository.user;


import juro.copyjuro.repository.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsById(Long id);

    Optional<User> findUserById(long id);

    Optional<User> findByUsername(String username);
}
