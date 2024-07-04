package com.pard.record_on_be.tag.entity;


import com.pard.record_on_be.experiences.entity.Experiences;
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
    @Column(name = "id", columnDefinition = "INT")
    private Integer id;

    @Column(name = "name", nullable = false, columnDefinition = "TINYTEXT")
    private String name;

    @ManyToOne
    @JoinColumn(name = "experiences_id", referencedColumnName = "id")
    private Experiences experiences;

    @OneToOne(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Question question;
}
