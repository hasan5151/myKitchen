package com.yaros.kitchen.room.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DishesModel {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    public String id;
    public String name;
    public Long cookingTime;
    public Boolean isChecked = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public DishesModel(String id, String name, Long cookingTime, Boolean isChecked) {
        this.id = id;
        this.name = name;
        this.cookingTime = cookingTime;
        this.isChecked = isChecked;
    }
}
