package com.example.projectmanagement.Domain.Entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "\"Comments\"")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"", nullable = false)
    private Long id;

    @Column(name = "\"Text\"", nullable = false, length = 500)
    private String text;

    @Column(name = "\"TaskID\"", nullable = false)
    private Long taskID;

    @Column(name = "\"UserID\"", nullable = false)
    private Long userID;

    @Column(name = "\"CreatedOn\"", nullable = false)
    private Instant createdOn;

    @Column(name = "\"ModifiedOn\"")
    private Instant modifiedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTaskID() {
        return taskID;
    }

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

}