package org.study.subjectresource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.subjectresource.entity.Users;

import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {
     Optional<Users> findByUsername(String username);
     Optional<Users> findByEmail(String email);
     boolean existsByUsername(String username);
     boolean existsByEmail(String email);

}
