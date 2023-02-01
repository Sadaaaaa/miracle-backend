package com.example.miracle.user.model;

import com.example.miracle.image.model.Image;
import com.example.miracle.image.model.UserImage;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @OneToOne(cascade = CascadeType.ALL)
    private Image image;
    //    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
//    @CollectionTable(name = "user_role",
//            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "password", length = 1000)
    private String password;
    @JoinColumn(name = "user_image")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserImage userImage;
    @Column(name = "is_enabled")
    private boolean isEnabled;
    @Column(name = "activation_code")
    private String activationCode;

    @JsonCreator
    public User(Integer id) {
        this.id = id;
    }
}
