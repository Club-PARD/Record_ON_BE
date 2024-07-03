package com.pard.record_on_be.competency_tag.repo;

import com.pard.record_on_be.competency_tag.entity.CompetencyTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetencyTagRepo extends JpaRepository<CompetencyTag, Integer> {
}
