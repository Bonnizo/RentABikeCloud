package com.example.rentabike.ui.Rent;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rentabike.R;
import com.example.rentabike.database.entity.RentEntity;
import com.example.rentabike.ui.BikeActivity;
import com.example.rentabike.viewmodel.BikeViewModel;
import com.example.rentabike.viewmodel.RentViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Class AddRentActivity qui permet d'ajouter une réservation.
 * Reçoit l'id du vélo qui sera réseré par la class ShowBikeActivity.
 * On doit remplir le formulaire et execute un insert pour rajouter la réservation dans la base de données.
 */

public class AddRentActivity extends AppCompatActivity {


    public static final String RENT_EXTRA_ID_BIKE =
            "com.example.rentabike.ui.Rent.RENT_EXTRA_ID_BIKE";

    private int mYear, mMonth, mDay;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private TextView editTextDate;
    private EditText editTextNumber;

    private RentViewModel rentViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rent);
        rentViewModel = new RentViewModel(getApplication());
        editTextFirstName = findViewById(R.id.edit_rent_firstname);
        editTextLastName = findViewById(R.id.edit_rent_lastname);
        editTextDate = findViewById(R.id.edit_rent_date);
        editTextNumber = findViewById(R.id.edit_rent_number);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add a new Rent");

        final ImageButton pickDate = (ImageButton) findViewById(R.id.pick_date);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // myCalendar.add(Calendar.DATE, 0);
                String myFormat = "dd.MM.yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                editTextDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(AddRentActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox

                        if (year < mYear)
                            view.updateDate(mYear, mMonth, mDay);

                        if (monthOfYear < mMonth && year == mYear)
                            view.updateDate(mYear, mMonth, mDay);

                        if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                            view.updateDate(mYear, mMonth, mDay);

                        editTextDate.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });
    }


    private void saveRent() {

        String firstname = editTextFirstName.getText().toString();
        String lastname = editTextLastName.getText().toString();
        String date = editTextDate.getText().toString();
        String number = editTextNumber.getText().toString();

        if (firstname.isEmpty() || lastname.isEmpty() || date.isEmpty() || number.isEmpty()) {
            Toast.makeText(this, "Please complete all data", Toast.LENGTH_SHORT).show();
            return;
        }

        String idBike = getIntent().getStringExtra(RENT_EXTRA_ID_BIKE);

        RentEntity rent = new RentEntity(firstname, lastname, date, number, idBike);

        rentViewModel.insert(rent);
        Toast.makeText(this, "Book added", Toast.LENGTH_SHORT).show();

        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.rent_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_rent:
                saveRent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
