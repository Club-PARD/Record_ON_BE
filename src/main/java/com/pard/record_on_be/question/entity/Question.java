package com.pard.record_on_be.question.entity;


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
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "INT")
    private Integer id;

    @Column(name = "tag_id", columnDefinition = "INT")
    private Integer tag_id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
}
