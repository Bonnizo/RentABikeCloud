package com.example.rentabike.database.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.rentabike.database.LiveData.BikeListLiveData;
import com.example.rentabike.database.entity.BikeEntity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Class BikeRepository permet d'effectuer les actions
 * On peut faire les méthodes update/delete/insert sans devoir mettre en pause le thread principal
 * Avec un appl de méthode insert/delete/insert, on lance une méthode AsyncTask qui exécutera en background la méthode rattachée.
 * Cela ne ralenti pas le programme et effectue les fonctions appelées.
 */
public class BikeRepository {


    private LiveData<List<BikeEntity>> allBikes;

    public BikeRepository(Application application) {

        allBikes = getAllBikes();
    }


    public void insert(BikeEntity bike) {
        String id = FirebaseDatabase.getInstance().getReference("bikes").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("bikes")
                .child(id)
                .setValue(bike);
    }

    public void update(BikeEntity bike, String Id) {
        FirebaseDatabase.getInstance()
                .getReference("bikes")
                .child(Id)
                .updateChildren(bike.toMap());
    }

    public void delete(BikeEntity bike) {
        FirebaseDatabase.getInstance()
                .getReference("bikes")
                .child(bike.getId())
                .removeValue();
    }


    public LiveData<List<BikeEntity>> getAllBikes() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("bikes");
        return new BikeListLiveData(reference);
    }


}