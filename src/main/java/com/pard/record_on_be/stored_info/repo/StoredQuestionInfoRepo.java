package com.pard.record_on_be.stored_info.repo;

import com.pard.record_on_be.stored_info.entity.StoredQuestionInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredQuestionInfoRepo extends JpaRepository<StoredQuestionInfo, Long> {
}
