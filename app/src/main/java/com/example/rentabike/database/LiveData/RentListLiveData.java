package com.example.rentabike.database.LiveData;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.rentabike.database.entity.BikeEntity;
import com.example.rentabike.database.entity.RentEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RentListLiveData extends LiveData<List<RentEntity>> {


    private static final String TAG = "BikeAccountsLiveData";
    private final DatabaseReference reference;
    private final RentListLiveData.MyValueEventListener listener = new RentListLiveData.MyValueEventListener();

    public RentListLiveData(DatabaseReference ref) {
        reference = ref;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toRentList(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<RentEntity> toRentList(DataSnapshot snapshot) {
        List<RentEntity> rents = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            RentEntity entity = childSnapshot.getValue(RentEntity.class);
            entity.setRentId(childSnapshot.getKey());
            rents.add(entity);
        }
        return rents;
    }


}
