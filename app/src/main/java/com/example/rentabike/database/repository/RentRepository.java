package com.example.rentabike.database.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import com.example.rentabike.database.LiveData.BikeListLiveData;
import com.example.rentabike.database.LiveData.RentListLiveData;
import com.example.rentabike.database.entity.RentEntity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Class RentRepository permet d'effectuer les actions
 * On peut faire les méthodes update/delete/insert sans devoir mettre en pause le thread principal
 * Avec un appl de méthode insert/delete/insert, on lance une méthode AsyncTask qui exécutera en background la méthode rattachée.
 * Cela ne ralenti pas le programme et effectue les fonctions appelées.
 */
public class RentRepository {


    private LiveData<List<RentEntity>> allRents;

    public RentRepository(Application application) {

        allRents = getAllRents();
    }


    public void insert(RentEntity rent) {
        String id = FirebaseDatabase.getInstance().getReference("rents").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("rents")
                .child(id)
                .setValue(rent);
    }

    public void update(RentEntity rent, String Id) {
        FirebaseDatabase.getInstance()
                .getReference("rents")
                .child(Id)
                .updateChildren(rent.toMap());
    }

    public void delete(RentEntity rent) {
        FirebaseDatabase.getInstance()
                .getReference("rents")
                .child(rent.getRentId())
                .removeValue();
    }


    public LiveData<List<RentEntity>> getAllRents() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("rents");
        return new RentListLiveData(reference);
    }

}
