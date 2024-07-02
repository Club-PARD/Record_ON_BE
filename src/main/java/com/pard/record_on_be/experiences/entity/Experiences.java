package com.pard.record_on_be.experiences.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "experiences")
public class Experiences {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    private Integer id;

    @Column(name = "project_id", columnDefinition = "TINYINT UNSIGNED")
    private Integer project_id;

    @Column(name = "title", columnDefinition = "TINYTEXT")
    private String title;

    @Column(name = "exp_date", columnDefinition = "DATE")
    private Date exp_date;
}
