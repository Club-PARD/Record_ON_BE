package com.pard.record_on_be.answer_histories.entity;

import com.pard.record_on_be.experiences.entity.Experiences;
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
@Table(name = "answer_histories")
public class AnswerHistories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "INT")
    private Integer id;

    @Column(name = "question_id", columnDefinition = "INT")
    private Integer question_id;

    @Column(name = "tag_id", columnDefinition = "INT")
    private Integer tag_id;

    @Column(name = "experience_id", columnDefinition = "INT")
    private Integer experience_id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "experience_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Experiences experiences;

}
