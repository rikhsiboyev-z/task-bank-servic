package com.example.banksystem.listeners;

import com.example.banksystem.user.entity.User;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class UserCreatedUpdated {

    @PrePersist
    public void created(User user) {
        user.setCreated(LocalDateTime.now());
    }

    @PreUpdate
    public void updated(User user) {
        user.setUpdated(LocalDateTime.now());
    }


}
