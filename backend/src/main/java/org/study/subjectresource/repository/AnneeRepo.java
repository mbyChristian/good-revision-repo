package org.study.subjectresource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.subjectresource.entity.Annee;

@Repository
public interface AnneeRepo extends JpaRepository<Annee, Long> {
}
