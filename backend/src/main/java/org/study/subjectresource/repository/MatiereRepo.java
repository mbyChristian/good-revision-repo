package org.study.subjectresource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.study.subjectresource.entity.Matiere;
@Repository
public interface MatiereRepo extends JpaRepository<Matiere, Long> {
}
