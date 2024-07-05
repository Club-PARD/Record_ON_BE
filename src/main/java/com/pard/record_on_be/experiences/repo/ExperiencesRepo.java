package com.pard.record_on_be.experiences.repo;

import com.pard.record_on_be.experiences.entity.Experiences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperiencesRepo extends JpaRepository<Experiences, Integer> {

}
