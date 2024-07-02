package com.pard.record_on_be.tag.repo;

import com.pard.record_on_be.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends JpaRepository<Tag, Integer> {
}
