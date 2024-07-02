package com.pard.record_on_be.answer_histories.repo;

import com.pard.record_on_be.answer_histories.entity.AnswerHistories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerHistoriesRepo extends JpaRepository<AnswerHistories, Integer> {
}
