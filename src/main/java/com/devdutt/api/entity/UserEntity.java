package com.devdutt.api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity
@Table(name = "Users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_email")
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
                 name = "roles",
                 joinColumns = @JoinColumn(name = "user_id")
                 )
    @Column(name = "user_roles")
    private List<String> roles;
}
