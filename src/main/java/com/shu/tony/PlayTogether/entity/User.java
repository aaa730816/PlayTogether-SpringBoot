package com.shu.tony.PlayTogether.entity;

import lombok.Data;

import javax.persistence.*;

@Table
@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = true)
    private String password;
    @Column(nullable = true)
    private String nickName;
    @Column(nullable = true)
    private String loginType;

}
