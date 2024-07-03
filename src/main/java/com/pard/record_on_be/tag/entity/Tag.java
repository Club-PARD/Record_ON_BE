package com.pard.record_on_be.tag.entity;


import com.pard.record_on_be.exp_tag_connections.entity.ExpTagConnections;
import com.pard.record_on_be.question.entity.Question;
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
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "SMALLINT UNSIGNED")
    private Integer id;

    @Column(name = "name", nullable = false, columnDefinition = "TINYTEXT")
    private String name;

    @OneToOne
    @JoinColumn(name = "exgTagConnections", referencedColumnName = "id")
    private ExpTagConnections expTagConnections;

    @OneToOne(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Question question;
}
