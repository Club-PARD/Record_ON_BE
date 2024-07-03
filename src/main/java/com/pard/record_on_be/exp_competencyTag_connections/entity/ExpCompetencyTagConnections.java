package com.pard.record_on_be.exp_competencyTag_connections.entity;

import com.pard.record_on_be.projects.entity.Projects;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "exp_competency_tag_connections")
public class ExpCompetencyTagConnections {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "INT")
    private Integer id;

    @Column(name = "project_id", columnDefinition = "INT")
    private Integer project_id;

    @Column(name = "competency_tag_id", columnDefinition = "INT")
    private Integer competency_tag_id;

    @ManyToOne
    @JoinColumn(name = "projects")
    private Projects projects;
}
