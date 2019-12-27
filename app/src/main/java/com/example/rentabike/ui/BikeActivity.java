package com.example.rentabike.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.rentabike.R;
import com.example.rentabike.ui.Bike.ShowBikeActivity;
import com.example.rentabike.adapter.BikeAdapter;
import com.example.rentabike.database.entity.BikeEntity;
import com.example.rentabike.ui.Bike.AddBikeActivity;
import com.example.rentabike.ui.Bike.EditBikeActivity;
import com.example.rentabike.viewmodel.BikeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * Class BikeActivity qui affiche la list de velo et sert de base pour effectuer les différents ajouts/modifications de bike dans le programme.
 * Permet de naviguer sur les autres actions disponibles pour le bike depuis chaque bike item dans la liste
 * On peut delete/update/insert un bike
 */

public class BikeActivity extends AppCompatActivity {


    public BikeViewModel bikeListViewModel;
    private List<BikeEntity> bikes;


    public static final int ADD_BIKE_REQUEST = 1;
    public static final int EDIT_BIKE_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike);
        setTitle("My bikes");
        BikeAdapter bikeAdapter = new BikeAdapter();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(bikeAdapter);

        FloatingActionButton buttonAddBike = findViewById(R.id.button_add_bike);
        buttonAddBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BikeActivity.this, AddBikeActivity.class);
                startActivityForResult(intent, ADD_BIKE_REQUEST);
            }
        });


        bikeListViewModel = ViewModelProviders.of(this).get(BikeViewModel.class);
        bikeListViewModel.getAllbikes().observe(this, new Observer<List<BikeEntity>>() {
            @Override
            public void onChanged(@Nullable List<BikeEntity> bikeEntities) {
                //update RecyclerView
                bikeAdapter.setBikeEntities(bikeEntities);
            }
        });


        bikeAdapter.setOnItemClickListener(new BikeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BikeEntity bike) {
                Intent intent = new Intent(BikeActivity.this, ShowBikeActivity.class);

                intent.putExtra(ShowBikeActivity.SHOW_EXTRA_NAME, bike.getName());
                intent.putExtra(ShowBikeActivity.SHOW_EXTRA_DESCRIPTION, bike.getDescription());
                intent.putExtra(ShowBikeActivity.SHOW_EXTRA_SIZE, bike.getSize());
                intent.putExtra(ShowBikeActivity.SHOW_EXTRA_PICTURE, bike.getImage());
                intent.putExtra(ShowBikeActivity.SHOW_EXTRA_ID, bike.getId());

                startActivity(intent);
            }

            @Override
            public void onEditClick(BikeEntity bike) {

                Intent intent = new Intent(BikeActivity.this, EditBikeActivity.class);

                intent.putExtra(EditBikeActivity.EDIT_EXTRA_ID, bike.getId());
                intent.putExtra(EditBikeActivity.EDIT_EXTRA_NAME, bike.getName());
                intent.putExtra(EditBikeActivity.EDIT_EXTRA_DESCRIPTION, bike.getDescription());
                intent.putExtra(EditBikeActivity.EDIT_EXTRA_SIZE, bike.getSize());
                intent.putExtra(EditBikeActivity.EDIT_EXTRA_PICTURE, bike.getImage());
                intent.putExtra(EditBikeActivity.EDIT_EXTRA_ID, bike.getId());
                startActivityForResult(intent, EDIT_BIKE_REQUEST);
            }

            public void onDeleteClick(BikeEntity bike) {
                AlertDialog diaBox = AskOption(bike);
                diaBox.show();
            }
        });

    }


    private AlertDialog AskOption(BikeEntity bike) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do you want to Delete ?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        dialog.dismiss();
                        bikeListViewModel.delete(bike);
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == EDIT_BIKE_REQUEST && resultCode == RESULT_OK) {
            String idBike = data.getStringExtra(EditBikeActivity.EDIT_EXTRA_ID);

            if (idBike == "null") {
                Toast.makeText(this, "There is a problem, bike not updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(EditBikeActivity.EDIT_EXTRA_NAME);
            String description = data.getStringExtra(EditBikeActivity.EDIT_EXTRA_DESCRIPTION);
            String size = data.getStringExtra(EditBikeActivity.EDIT_EXTRA_SIZE);
            String picture = data.getStringExtra(EditBikeActivity.EDIT_EXTRA_PICTURE);

            BikeEntity bikeEntity = new BikeEntity(name, description, size, picture);


            bikeListViewModel.update(bikeEntity, idBike);
            Intent intent = new Intent(BikeActivity.this, BikeActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                Intent intentHelp = new Intent(this, AboutActivity.class);
                this.startActivity(intentHelp);
                break;

            case R.id.action_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                this.startActivity(intentSettings);
                break;
            case R.id.action_rent:
                Intent intentRent = new Intent(this, RentActivity.class);
                this.startActivity(intentRent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

}