package com.pard.record_on_be.competency_tag.entity;

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

    @Column(name = "competency_tag_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "projects_id", referencedColumnName = "id")
    private Projects projects;
}
