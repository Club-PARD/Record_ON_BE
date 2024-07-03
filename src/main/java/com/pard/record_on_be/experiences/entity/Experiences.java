package com.pard.record_on_be.experiences.entity;

import com.pard.record_on_be.exp_ans_connections.entity.ExpAnsConnections;
import com.pard.record_on_be.exp_tag_connections.entity.ExpTagConnections;
import com.pard.record_on_be.free_content.entity.FreeContent;
import com.pard.record_on_be.projects.entity.Projects;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "experiences")
public class Experiences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    private Integer id;

    @Column(name = "project_id", columnDefinition = "TINYINT UNSIGNED")
    private Integer project_id;

    @Column(name = "title", columnDefinition = "TINYTEXT")
    private String title;

    @Column(name = "exp_date", columnDefinition = "DATE")
    private Date exp_date;

    @OneToOne(mappedBy = "experiences", cascade = CascadeType.ALL, orphanRemoval = true)
    private FreeContent freeContent;

    @OneToMany(mappedBy = "experiences", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpTagConnections> expTagConnections;

    @OneToMany(mappedBy = "experiences", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpAnsConnections> expAnsConnections;

    @ManyToOne
    @JoinColumn(name = "projects", referencedColumnName = "id")
    private Projects projects;
}
