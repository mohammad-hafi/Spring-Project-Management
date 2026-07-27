package com.example.projectmanagement.Domain.Entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "\"Audits\"")
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"UserID\"", nullable = false)
    private User userID;

    @Column(name = "\"EventID\"", nullable = false)
    private Integer eventID;

    @Column(name = "\"ActionID\"", nullable = false)
    private Integer actionID;

    @Column(name = "\"CreatedOn\"", nullable = false)
    private Instant createdOn;

    @Column(name = "\"BeforeWas\"", nullable = false, length = Integer.MAX_VALUE)
    private String beforeWas;

    @Column(name = "\"AfterIs\"", nullable = false, length = Integer.MAX_VALUE)
    private String afterIs;

    @Column(name = "\"StatusID\"", nullable = false)
    private Integer statusID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public Integer getActionID() {
        return actionID;
    }

    public void setActionID(Integer actionID) {
        this.actionID = actionID;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getBeforeWas() {
        return beforeWas;
    }

    public void setBeforeWas(String beforeWas) {
        this.beforeWas = beforeWas;
    }

    public String getAfterIs() {
        return afterIs;
    }

    public void setAfterIs(String afterIs) {
        this.afterIs = afterIs;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

}