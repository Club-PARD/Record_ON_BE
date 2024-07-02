package com.pard.record_on_be.exp_tag_connections.entity;

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
@Table(name = "exp_tag_connections")
public class ExpTagConnections {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "INT")
    private Integer id;

    @Column(name = "experience_id", columnDefinition = "INT")
    private Integer experience_id;

    @Column(name = "tag_id", columnDefinition = "INT")
    private Integer tag_id;
}
