package com.example.rentabike.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Class RentEntity pour la création de la table rents dans la base de données
 * Il y a également le constructeur pour pouvoir créer des réservations dans le programme
 * et les get/set afin de passer ou recevoir les paramètres des réservations
 */


public class RentEntity {


    private String rentId;
    private String firstNameClient;
    private String lastNameClient;
    private String clientNumber;
    private String dateRent;
    private String bikeRentedId;

    public RentEntity() {
    }

    public RentEntity(String firstNameClient, String lastNameClient, String dateRent, String clientNumber, String bikeRentedId) {
        this.firstNameClient = firstNameClient;
        this.lastNameClient = lastNameClient;
        this.dateRent = dateRent;
        this.clientNumber = clientNumber;
        this.bikeRentedId = bikeRentedId;
    }

    @Exclude
    public String getRentId() {
        return rentId;
    }

    public void setRentId(String rentId) {
        this.rentId = rentId;
    }

    public String getFirstNameClient() {
        return firstNameClient;
    }

    public void setFirstNameClient(String FirstnameClient) {
        this.firstNameClient = firstNameClient;
    }

    public String getLastNameClient() {
        return lastNameClient;
    }

    public void setLastNameClient(String lastNameClient) {
        this.lastNameClient = lastNameClient;
    }


    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }


    public String getDateRent() {
        return dateRent;
    }

    public void setDateRent(String dateRent) {
        this.dateRent = dateRent;
    }


    public String getBikeRentedId() {
        return bikeRentedId;
    }


    public void setbikeRentedId(String bikeRentedId) {
        this.bikeRentedId = bikeRentedId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("rentId", rentId);
        result.put("firstNameClient", firstNameClient);
        result.put("lastNameClient", lastNameClient);
        result.put("clientNumber", clientNumber);
        result.put("dateRent", dateRent);
        result.put("bikeRentId", bikeRentedId);
        return result;
    }
}