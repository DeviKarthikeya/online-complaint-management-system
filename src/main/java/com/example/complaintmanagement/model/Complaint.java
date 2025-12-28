package com.example.complaintmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String status;

    // Stores full admin reply history (chat-like)
    @Column(length = 5000)
    private String adminReplyHistory;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Complaint() {
    }

    public Complaint(String description, String status, User user) {
        this.description = description;
        this.status = status;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getAdminReplyHistory() {
        return adminReplyHistory;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAdminReplyHistory(String adminReplyHistory) {
        this.adminReplyHistory = adminReplyHistory;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
