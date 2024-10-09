package com.bezkoder.spring.security.postgresql.Objets;

import java.util.ArrayList;
import java.util.List;

public class InviteRequest {
    private List<String> userEmails = new ArrayList<>();
    private Long groupId;

    // Getters y setters
    public List<String> getUserEmails() {
        return userEmails;
    }

    public void setUserEmails(List<String> userEmails) {
        this.userEmails = userEmails;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}