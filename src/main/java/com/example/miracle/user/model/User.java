package com.example.miracle.user.model;

import com.example.miracle.image.model.Image;
import com.example.miracle.image.model.UserImage;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
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
//    @Column(name = "user_role")
//    @Enumerated(EnumType.STRING)
//    private Role role;
    @Column(name = "login", length = 1000)
    private String login;
    @Column(name = "password", length = 1000)
    private String password;
    @JoinColumn(name = "user_image")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private UserImage userImage;
    @Column(name = "is_enabled")
    private boolean isEnabled;
    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "firstname")
    private String firstname;

    @Transient
    private Set<Role> roles = new HashSet<>();

    @JsonCreator
    public User(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

//    public void setRole(Role roleUser) {
//    }
}
