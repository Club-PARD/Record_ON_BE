package com.pard.record_on_be.user.entity;

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

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
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

    @Column(name = "job_id", nullable = false, columnDefinition = "TINYINT")
    private Integer job_id;

    public User update(String name){
        this.name = name;
        return this;
    }

    public static User toEntity(UserDTO.Create dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .picture(dto.getPicture())
                .job_id(dto.getJob_id())
                .build();
    }
}
