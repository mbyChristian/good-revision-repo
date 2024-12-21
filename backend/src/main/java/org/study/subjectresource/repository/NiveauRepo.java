package org.study.subjectresource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.subjectresource.entity.Niveau;

@Repository
public interface NiveauRepo extends JpaRepository<Niveau, Long> {
}
