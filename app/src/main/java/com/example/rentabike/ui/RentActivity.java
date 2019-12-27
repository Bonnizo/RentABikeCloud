package com.example.rentabike.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.rentabike.R;
import com.example.rentabike.adapter.RentAdapter;
import com.example.rentabike.database.entity.BikeEntity;
import com.example.rentabike.database.entity.RentEntity;
import com.example.rentabike.ui.Bike.ShowBikeActivity;
import com.example.rentabike.ui.Rent.EditRentActivity;
import com.example.rentabike.viewmodel.RentViewModel;

import java.util.List;


/**
 * Class RentActivity qui affiche la liste de réservation et sert de base pour effectuer les différents modifications
 * de réservation dans le programme.
 * Permet de naviguer sur les autres actions disponibles pour la réservation depuis chaque réservation item dans la liste
 * La liste update si on a un onchange. Possibilité d'edit une réservation également en récupérant les données de EditRentActivity
 */
public class RentActivity extends AppCompatActivity {


    public static final int EDIT_RENT_REQUEST = 2;
    private RentViewModel rentListViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        setTitle("My rentals");
        RentAdapter rentAdapter = new RentAdapter();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(rentAdapter);


        rentListViewModel = ViewModelProviders.of(this).get(RentViewModel.class);
        rentListViewModel.getAllrent().observe(this, new Observer<List<RentEntity>>() {
            @Override
            public void onChanged(@Nullable List<RentEntity> rentEntities) {
                rentAdapter.setrentEntities(rentEntities);
            }
        });


        rentAdapter.setOnItemClickListener(new RentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RentEntity rent) {
            }

            public void onEditClick(RentEntity rent) {

                Intent intent = new Intent(RentActivity.this, EditRentActivity.class);

                intent.putExtra(EditRentActivity.EDIT_EXTRA_FIRSTNAME, rent.getFirstNameClient());
                intent.putExtra(EditRentActivity.EDIT_EXTRA_LASTNAME, rent.getLastNameClient());
                intent.putExtra(EditRentActivity.EDIT_EXTRA_DATE, rent.getDateRent());
                intent.putExtra(EditRentActivity.EDIT_EXTRA_NUMBER, rent.getClientNumber());
                intent.putExtra(EditRentActivity.EDIT_EXTRA_RENTID, rent.getRentId());
                intent.putExtra(EditRentActivity.EDIT_EXTRA_RENTBIKEID, rent.getBikeRentedId());


                startActivityForResult(intent, EDIT_RENT_REQUEST);
            }

            public void onDeleteClick(RentEntity rent) {
                AlertDialog diaBox = AskOption(rent);
                diaBox.show();
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == EDIT_RENT_REQUEST && resultCode == RESULT_OK) {
            String idRent = data.getStringExtra(EditRentActivity.EDIT_EXTRA_RENTID);
            String idBikeRented = data.getStringExtra(EditRentActivity.EDIT_EXTRA_RENTBIKEID);

            if (idRent == "null") {
                Toast.makeText(this, "There is a problem, rent not updated", Toast.LENGTH_SHORT).show();
                return;
            }
            if (idBikeRented == "null") {
                Toast.makeText(this, "There is no bike rented", Toast.LENGTH_SHORT).show();
                return;
            }

            String firstname = data.getStringExtra(EditRentActivity.EDIT_EXTRA_FIRSTNAME);
            String lastname = data.getStringExtra(EditRentActivity.EDIT_EXTRA_LASTNAME);
            String date = data.getStringExtra(EditRentActivity.EDIT_EXTRA_DATE);
            String number = data.getStringExtra(EditRentActivity.EDIT_EXTRA_NUMBER);

            RentEntity rentEntity = new RentEntity(firstname, lastname, date, number, idBikeRented);


            rentListViewModel.update(rentEntity, idRent);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main2, menu);
        MenuItem item = menu.findItem(R.id.action_search);

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
            case R.id.action_bike:
                Intent intentRent = new Intent(this, BikeActivity.class);
                this.startActivity(intentRent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private AlertDialog AskOption(RentEntity rent) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete ?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        dialog.dismiss();
                        rentListViewModel.delete(rent);
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


}
