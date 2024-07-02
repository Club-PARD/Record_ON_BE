package com.pard.record_on_be.exp_ans_connections.entity;

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
@Table(name = "exp_ans_connections")
public class ExpAnsConnections {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "INT")
    private Integer id;

    @Column(name = "experience_id", columnDefinition = "INT")
    private Integer experience_id;

    @Column(name = "answer_id", columnDefinition = "INT")
    private Integer answer_id;
}
