package com.pard.record_on_be.free_content.repo;


import com.pard.record_on_be.free_content.entity.FreeContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeContentRepo extends JpaRepository<FreeContent, Integer> {
}
