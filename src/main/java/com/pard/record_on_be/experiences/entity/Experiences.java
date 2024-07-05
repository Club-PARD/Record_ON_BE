package com.pard.record_on_be.experiences.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.record_on_be.answer_histories.entity.AnswerHistories;
import com.pard.record_on_be.projects.entity.Projects;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "experiences")
public class Experiences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "INT")
    private Integer id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID user_id;

    @Column(name = "projects_id", columnDefinition = "INT")
    private Integer projects_id;

    @Column(name = "title", columnDefinition = "TINYTEXT")
    private String title;

    @Column(name = "exp_date", columnDefinition = "DATE")
    private Date exp_date;

    /*
        jsoninclude로 null일떄 테이블이 추가되지 않도록 함.
        내용이 없으면 프런트에서 null 처리 해서 넘겨줘야 함.
     */
    @Column(name = "free_content", columnDefinition = "TEXT")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String free_content;

    @Column(name = "common_question_answer", columnDefinition = "TEXT")
    private String common_question_answer;


    @OneToMany(mappedBy = "experiences", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerHistories> answerHistoriesList;

    @ManyToOne
    @JoinColumn(name = "projects", referencedColumnName = "id")
    private Projects projects;

    public void setProjects(Projects projects) {
        this.projects = projects;
        this.projects_id = projects.getId();
    }
}
