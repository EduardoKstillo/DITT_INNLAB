package com.bezkoder.spring.security.postgresql.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "entry_exit_records")
public class EntryExitRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    private Date timestamp;

    @Column(name = "entry")
    private boolean entry;

    public EntryExitRecord() {
    }

    public EntryExitRecord(User user, Date timestamp, boolean entry) {
        this.user = user;
        this.timestamp = timestamp;
        this.entry = entry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isEntry() {
        return entry;
    }

    public void setEntry(boolean entry) {
        this.entry = entry;
    }
}
