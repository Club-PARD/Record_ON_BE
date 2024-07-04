package com.pard.record_on_be.utill.repo;

import com.pard.record_on_be.utill.entity.StoredTagInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredTagInfoRepo extends JpaRepository<StoredTagInfo, Integer> {
}
