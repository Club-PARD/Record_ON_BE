package com.pard.record_on_be.stored_info.repo;

import com.pard.record_on_be.stored_info.entity.StoredTagInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredTagInfoRepo extends JpaRepository<StoredTagInfo, Integer> {
}
