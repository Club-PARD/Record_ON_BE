package com.pard.record_on_be.exp_tag_connections.entity;

import com.pard.record_on_be.experiences.entity.Experiences;
import com.pard.record_on_be.tag.entity.Tag;
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
@Table(name = "exp_tag_connections")
public class ExpTagConnections {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "INT")
    private Integer id;

    @Column(name = "experience_id", columnDefinition = "INT")
    private Integer experience_id;

    @Column(name = "tag_id", columnDefinition = "INT")
    private Integer tag_id;

    @ManyToOne
    @JoinColumn(name = "experience")
    private Experiences experiences;

    @OneToOne(mappedBy = "expTagConnections", cascade = CascadeType.ALL, orphanRemoval = true)
    private Tag tag;
}
