package com.pard.record_on_be.free_content.entity;

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
@Table(name = "free_content")
public class FreeContent {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "SMALLINT UNSIGNED")
    private Integer id;

    @Column(name = "experience_id", columnDefinition = "SMALLINT UNSIGNED")
    private Integer experience_id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
}
