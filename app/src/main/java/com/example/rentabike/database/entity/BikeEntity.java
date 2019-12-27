package com.example.rentabike.database.entity;


import android.media.Image;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Class BikeEntity pour la création de la table bikes dans la base de données
 * Il y a également le constructeur pour pouvoir créer des bikes dans le programme
 * et les get/set afin de passer ou recevoir les paramètres des bikes
 */


public class BikeEntity {


    private String id;
    private String name;
    private String description;
    private String size;
    private String image;


    public BikeEntity() {
    }

    public BikeEntity(String name, String description, String size, String image) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.image = image;

    }

    @Exclude
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("description", description);
        result.put("size", size);
        result.put("image", image);
        return result;
    }


}
