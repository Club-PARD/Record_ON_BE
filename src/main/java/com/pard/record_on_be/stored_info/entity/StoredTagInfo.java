package com.pard.record_on_be.utill.entity;


import com.pard.record_on_be.utill.entity.StoredQuestionInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "stored_tag_info")
public class StoredTagInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT")
    private Integer id;

    @Column(name = "tag_name", nullable = false, updatable = false, columnDefinition = "TEXT")
    private String tagName;

    @OneToMany(mappedBy = "storedTagInfo", cascade = CascadeType.ALL)
    private List<StoredQuestionInfo> questionInfoList;
}
