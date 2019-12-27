package com.example.rentabike.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.rentabike.database.entity.BikeEntity;
import com.example.rentabike.database.repository.BikeRepository;

import java.util.List;

/**
 * Class BikeViewModel permet de créé le viewModel pour la liste de vélo et de connecter au repository pour effectuer les actions
 * update/delete/insert et getAllBikes.
 * On pourra effectuer ses méthodes de manière asynchrone afin de ne pas ralentir le programme
 */
public class BikeViewModel extends AndroidViewModel {

    private BikeRepository repository;
    private LiveData<List<BikeEntity>> allBikes;

    public BikeViewModel(@NonNull Application application) {
        super(application);

        repository = new BikeRepository(application);
        allBikes = repository.getAllBikes();
    }

    public void insert(BikeEntity bike) {
        repository.insert(bike);
    }

    public void update(BikeEntity bike, String Id) {
        repository.update(bike, Id);
    }

    public void delete(BikeEntity bike) {
        repository.delete(bike);
    }

    public LiveData<List<BikeEntity>> getAllbikes() {
        return allBikes;
    }
}