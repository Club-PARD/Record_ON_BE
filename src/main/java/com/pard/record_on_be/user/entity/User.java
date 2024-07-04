package com.pard.record_on_be.user.entity;

import com.pard.record_on_be.projects.entity.Projects;
import com.pard.record_on_be.utill.BaseTimeEntity;
import com.pard.record_on_be.user.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, columnDefinition = "TINYTEXT")
    private String name;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "TINYTEXT")
    private String email;

    @Column(name = "picture", nullable = false, columnDefinition = "TEXT")
    private String picture;

    @Column(name = "job", nullable = false, columnDefinition = "TEXT")
    private String job;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Projects> projects;

    public User update(String name){
        this.name = name;
        return this;
    }

    public static User toEntity(UserDTO.Create dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .picture(dto.getPicture())
                .job(dto.getJob())
                .build();
    }
}
