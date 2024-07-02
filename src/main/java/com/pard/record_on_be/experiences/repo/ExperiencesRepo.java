package com.pard.record_on_be.experiences.repo;

import com.pard.record_on_be.experiences.entity.Experiences;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperiencesRepo extends CrudRepository<Experiences, Long> {
}
