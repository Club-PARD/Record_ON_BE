package com.pard.record_on_be.project_data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "project_data")
public class ProjectData {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    private Integer id;

    @Column(name = "experience_id", columnDefinition = "TINYINT UNSIGNED")
    private Integer experience_id;

    @Column(name = "resources_type", columnDefinition = "TINYINT UNSIGNED")
    private Integer resources_type;

    @Column(name = "references_link", columnDefinition = "TEXT")
    private String references_link;
}