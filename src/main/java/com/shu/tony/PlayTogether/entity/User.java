package com.shu.tony.PlayTogether.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table
@Entity
@Data
public class User extends BaseEntity {
    @Column(nullable = false)
    private String username;
    @Column(nullable = true)
    private String password;
    @Column(nullable = true)
    private String nickName;
    @Column(nullable = true)
    private String loginType;
    @JsonIgnore
    @ManyToMany(mappedBy = "participant")
    private Set<Activity> participateActivities=new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "tenant")
    private Set<Equipment> equipmentSet=new HashSet<>();

}
