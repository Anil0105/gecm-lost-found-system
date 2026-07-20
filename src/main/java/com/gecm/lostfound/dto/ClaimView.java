package com.gecm.lostfound.dto;

import com.gecm.lostfound.model.ClaimStatus;

import java.time.LocalDateTime;

public class ClaimView {

    private Integer id;
    private Integer itemId;
    private String claimerName;
    private String claimerEmail;
    private String message;
    private ClaimStatus status;
    private LocalDateTime createdAt;
    private String itemTitle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getClaimerName() {
        return claimerName;
    }

    public void setClaimerName(String claimerName) {
        this.claimerName = claimerName;
    }

    public String getClaimerEmail() {
        return claimerEmail;
    }

    public void setClaimerEmail(String claimerEmail) {
        this.claimerEmail = claimerEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
}
