package com.pard.record_on_be.stored_info.repo;

import com.pard.record_on_be.stored_info.entity.StoredCompetencyTagInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredCompetencyTagInfoRepo extends JpaRepository<StoredCompetencyTagInfo, Integer> {
}
