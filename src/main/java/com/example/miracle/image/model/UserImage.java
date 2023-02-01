package com.example.miracle.image.model;

import com.example.miracle.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Users_images")
@NoArgsConstructor
@AllArgsConstructor
public class UserImage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "filename")
    private String filename;
    @Column(name = "size")
    private Long size;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "path")
    private String path;
    @OneToOne
    @JsonIgnore
    private User user;
}
