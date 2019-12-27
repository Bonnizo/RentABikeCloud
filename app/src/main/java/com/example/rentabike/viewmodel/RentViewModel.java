package com.example.rentabike.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.rentabike.database.entity.BikeEntity;
import com.example.rentabike.database.entity.RentEntity;
import com.example.rentabike.database.repository.RentRepository;

import java.util.List;

/**
 * Class RentViewModel permet de créé le viewModel pour la liste de réservations et de connecter au repository pour effectuer les actions
 * update/delete/insert et getAllrents.
 * On pourra effectuer ses méthodes de manière asynchrone afin de ne pas ralentir le programme
 */
public class RentViewModel extends AndroidViewModel {

    private RentRepository repository;
    private LiveData<List<RentEntity>> allrents;

    public RentViewModel(@NonNull Application application) {
        super(application);

        repository = new RentRepository(application);
        allrents = repository.getAllRents();
    }

    public void insert(RentEntity rent) {
        repository.insert(rent);
    }

    public void update(RentEntity rent, String Id) {
        repository.update(rent, Id);
    }

    public void delete(RentEntity rent) {
        repository.delete(rent);
    }

    public LiveData<List<RentEntity>> getAllrent() {
        return allrents;
    }
}
