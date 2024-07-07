package com.pard.record_on_be.stored_info.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "stored_competency_tag_info")
public class StoredCompetencyTagInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT")
    private Integer id;

    @Column(name = "competency_tag_name", nullable = false, updatable = false, columnDefinition = "TEXT")
    private String competencyTagName;

}
