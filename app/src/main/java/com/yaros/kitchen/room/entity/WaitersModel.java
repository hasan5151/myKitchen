package com.yaros.kitchen.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WaitersModel {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    public String id;
    public String name;
    public Boolean delete = false;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public WaitersModel(@NonNull String id, String name, Boolean delete) {
        this.id = id;
        this.name = name;
        this.delete = delete;
    }
}
