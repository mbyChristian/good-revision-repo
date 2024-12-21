package org.study.subjectresource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.study.subjectresource.entity.Sujets;

import java.util.List;

@Repository
public interface SubjectRepo extends JpaRepository<Sujets, Long> {

    @Query("select distinct s from Sujets s join s.user")
    List<Sujets> allSubjectDownload();
}
