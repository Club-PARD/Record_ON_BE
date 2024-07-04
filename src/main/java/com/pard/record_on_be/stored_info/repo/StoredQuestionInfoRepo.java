package com.pard.record_on_be.utill.repo;

import com.pard.record_on_be.utill.entity.StoredQuestionInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredQuestionInfoRepo extends JpaRepository<StoredQuestionInfo, Long> {
}
