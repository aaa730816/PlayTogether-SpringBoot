package com.shu.tony.PlayTogether.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table
@Entity
public class Activity extends BaseEntity {
    @Column
    private String title;
    @Column
    private Date startTime;
    @Embedded
    private GeoLocation location;
    private String cost;
    private int numOfPeople;
    private Boolean needBringEquipment = false;
    private String description;
    private String type;
    private String game;
    @ManyToMany
    @JoinTable(name = "activity_user", joinColumns = {
            @JoinColumn(name = "activity_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Set<User> participant=new HashSet<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public int getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(int numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public Boolean getNeedBringEquipment() {
        return needBringEquipment;
    }

    public void setNeedBringEquipment(Boolean needBringEquipment) {
        this.needBringEquipment = needBringEquipment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<User> getParticipant() {
        return participant;
    }
    @JsonBackReference
    public void setParticipant(Set<User> participant) {
        this.participant = participant;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }
}
