package com.pard.record_on_be.competency_tag.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pard.record_on_be.projects.entity.Projects;
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
@Table(name = "competency_tag")
public class CompetencyTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "INT")
    private Integer id;

    @Column(name = "competency_tag_name", columnDefinition = "TINYTEXT")
    private String competencyTagName;

    @ManyToOne
    @JoinColumn(name = "projects_id", referencedColumnName = "id")
    @JsonIgnore // 순환 참조 방지
    private Projects projects;

    @Column(name = "stored_competency_tag_id", columnDefinition = "INT")
    private Integer storedCompetencyTagId;

    public void setProjects(Projects projects) {
        this.projects = projects;
    }
}
