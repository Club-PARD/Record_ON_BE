package com.pard.record_on_be.projects.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "projects")
public class Projects {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Integer id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID user_id;

    @Column(name = "name", nullable = false, columnDefinition = "TINYTEXT")
    private String name;

    @Column(name = "create_date", nullable = false, updatable = false)
    private Date create_date;

    @Column(name = "update_date", nullable = false)
    private Date update_date;

    @Column(name = "finish_date")
    private Date finish_date;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "picture", columnDefinition = "TEXT")
    private String picture;

    @Column(name = "is_finished", columnDefinition = "TINYINT")
    private Integer is_finished;
}
