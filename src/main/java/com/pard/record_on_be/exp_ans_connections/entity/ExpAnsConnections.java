package com.pard.record_on_be.exp_ans_connections.entity;

import com.pard.record_on_be.answer_histories.entity.AnswerHistories;
import com.pard.record_on_be.experiences.entity.Experiences;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "exp_ans_connections")
public class ExpAnsConnections {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "INT")
    private Integer id;

    @Column(name = "experience_id", columnDefinition = "INT")
    private Integer experience_id;

    @Column(name = "answer_id", columnDefinition = "INT")
    private Integer answer_id;

    @ManyToOne
    @JoinColumn(name = "experience")
    private Experiences experiences;

    @OneToOne
    @JoinColumn(name = "answer_histories_id", referencedColumnName = "id")
    private AnswerHistories answerHistories;
}
