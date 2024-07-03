package com.pard.record_on_be.answer_histories.entity;

import com.pard.record_on_be.exp_ans_connections.entity.ExpAnsConnections;
import com.pard.record_on_be.question.entity.Question;
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

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @OneToOne
    @JoinColumn(name = "question")
    private Question question;

    @OneToOne(mappedBy = "answerHistories", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExpAnsConnections expAnsConnections;
}
