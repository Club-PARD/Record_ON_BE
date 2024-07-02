package com.pard.record_on_be.tag.entity;


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
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "SMALLINT UNSIGNED")
    private Integer id;

    @Column(name = "name", nullable = false, columnDefinition = "TINYTEXT")
    private String name;
}
