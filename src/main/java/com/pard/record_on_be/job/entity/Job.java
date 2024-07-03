package com.pard.record_on_be.job.entity;

import com.pard.record_on_be.job.dto.JobDTO;
import com.pard.record_on_be.user.entity.User;
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
@Table(name = "job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "TINYINT")
    private Integer id;

    @Column(name = "name", nullable = false, columnDefinition = "TINYTEXT")
    private String name;

    @OneToOne(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

}
