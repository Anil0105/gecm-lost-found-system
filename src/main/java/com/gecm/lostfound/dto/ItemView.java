package com.gecm.lostfound.dto;

import com.gecm.lostfound.model.ItemStatus;
import com.gecm.lostfound.model.ItemType;

import java.time.LocalDateTime;

public class ItemView {

    private Integer id;
    private ItemType type;
    private String title;
    private String description;
    private String location;
    private String image;
    private Integer addedBy;
    private ItemStatus status;
    private LocalDateTime createdAt;
    private String addedName;
    private String addedEmail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddedName() {
        return addedName;
    }

    public void setAddedName(String addedName) {
        this.addedName = addedName;
    }

    public String getAddedEmail() {
        return addedEmail;
    }

    public void setAddedEmail(String addedEmail) {
        this.addedEmail = addedEmail;
    }
}
