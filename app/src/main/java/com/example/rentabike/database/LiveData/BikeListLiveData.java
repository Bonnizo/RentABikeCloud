package com.example.rentabike.database.LiveData;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.rentabike.database.entity.BikeEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BikeListLiveData extends LiveData<List<BikeEntity>> {

    private static final String TAG = "BikeAccountsLiveData";
    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    public BikeListLiveData(DatabaseReference ref) {
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
            setValue(toBikeList(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<BikeEntity> toBikeList(DataSnapshot snapshot) {
        List<BikeEntity> bikes = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            BikeEntity entity = childSnapshot.getValue(BikeEntity.class);
            entity.setId(childSnapshot.getKey());
            bikes.add(entity);
        }
        return bikes;
    }


}
