package com.example.projectmanagement.Domain.Entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "\"Tasks\"")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"", nullable = false)
    private Long id;

    @Column(name = "\"Name\"", nullable = false, length = 50)
    private String name;

    @Column(name = "\"Description\"", length = 50)
    private String description;

    @Column(name = "\"CreatedOn\"", nullable = false)
    private Instant createdOn;

    @Column(name = "\"ModifiedOn\"")
    private Instant modifiedOn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"StatusID\"", nullable = false)
    private Status statusID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"ProjectID\"", nullable = false)
    private Project projectID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"UserID\"", nullable = false)
    private User userID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"StageID\"", nullable = false)
    private Stage stageID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"AssignedUserID\"", nullable = false)
    private User assignedUserID;

    @Column(name = "\"Priority\"", nullable = false)
    private Integer priority;

    @Column(name = "\"DueDate\"")
    private Instant dueDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Status getStatusID() {
        return statusID;
    }

    public void setStatusID(Status statusID) {
        this.statusID = statusID;
    }

    public Project getProjectID() {
        return projectID;
    }

    public void setProjectID(Project projectID) {
        this.projectID = projectID;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    public Stage getStageID() {
        return stageID;
    }

    public void setStageID(Stage stageID) {
        this.stageID = stageID;
    }

    public User getAssignedUserID() {
        return assignedUserID;
    }

    public void setAssignedUserID(User assignedUserID) {
        this.assignedUserID = assignedUserID;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

}