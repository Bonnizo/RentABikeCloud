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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rentabike.R;
import com.example.rentabike.database.entity.RentEntity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Class EditRentActivity qui permet de modifier une réservation.
 * On reçoit les données de la class RentActivity
 * On peut modifier le formulaire et renvoyer les données à la class RentActivity pour sauvegarder les modifications et effectuer
 * un update
 */
public class EditRentActivity extends AppCompatActivity {

    public static final String EDIT_EXTRA_RENTID = "com.example.rentabike.ui.Rent.EDIT_EXTRA_RENTID";
    public static final String EDIT_EXTRA_FIRSTNAME = "com.example.rentabike.ui.Rent.EDIT_EXTRA_FIRSTNAME";
    public static final String EDIT_EXTRA_LASTNAME = "com.example.rentabike.ui.Rent.EDIT_EXTRA_LASTNAME";
    public static final String EDIT_EXTRA_DATE = "com.example.rentabike.ui.Rent.EDIT_EXTRA_DATE";
    public static final String EDIT_EXTRA_NUMBER = "com.example.rentabike.ui.Rent.EDIT_EXTRA_NUMBER";
    public static final String EDIT_EXTRA_RENTBIKEID = "com.example.rentabike.ui.Rent.EDIT_EXTRA_RENTBIKEID";

    private int mYear, mMonth, mDay;
    private EditText editTextFirstname;
    private EditText editTextLastname;
    private TextView editTextDate;
    private EditText editTextNumbero;
    private String dateTemp;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rent);

        editTextFirstname = findViewById(R.id.edit_rent_firstname);
        editTextLastname = findViewById(R.id.edit_rent_lastname);
        editTextDate = findViewById(R.id.edit_rent_date);
        editTextNumbero = findViewById(R.id.edit_rent_number);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        setTitle("Edit a Rent");
        //passer les infos du rent
        editTextFirstname.setText(intent.getStringExtra(EDIT_EXTRA_FIRSTNAME));
        editTextLastname.setText(intent.getStringExtra(EDIT_EXTRA_LASTNAME));
        editTextDate.setText(intent.getStringExtra(EDIT_EXTRA_DATE));
        editTextNumbero.setText(intent.getStringExtra(EDIT_EXTRA_NUMBER));
        dateTemp = intent.getStringExtra(EDIT_EXTRA_DATE);
        final ImageButton pickDate = (ImageButton) findViewById(R.id.pick_date);
        final TextView textView = (TextView) findViewById(R.id.edit_rent_date);
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
                textView.setText(sdf.format(myCalendar.getTime()));
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
                DatePickerDialog dpd = new DatePickerDialog(EditRentActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox

                        if (year < mYear)
                            view.updateDate(mYear, mMonth, mDay);

                        if (monthOfYear < mMonth && year == mYear)
                            view.updateDate(mYear, mMonth, mDay);

                        if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                            view.updateDate(mYear, mMonth, mDay);

                        textView.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });
    }

    private void saveRentEdit() {

        String firstname = editTextFirstname.getText().toString();
        String lastname = editTextLastname.getText().toString();
        String date = editTextDate.getText().toString();
        String number = editTextNumbero.getText().toString();

        if (firstname.isEmpty() || lastname.isEmpty() || date.isEmpty() || number.isEmpty()) {
            Toast.makeText(this, "Please complete all data", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();

        String idRent = getIntent().getStringExtra(EDIT_EXTRA_RENTID);
        String idBikerented = getIntent().getStringExtra(EDIT_EXTRA_RENTBIKEID);



        data.putExtra(EDIT_EXTRA_RENTID, idRent);
        data.putExtra(EDIT_EXTRA_RENTBIKEID, idBikerented);
        data.putExtra(EDIT_EXTRA_FIRSTNAME, firstname);
        data.putExtra(EDIT_EXTRA_LASTNAME, lastname);
        data.putExtra(EDIT_EXTRA_DATE, date);
        data.putExtra(EDIT_EXTRA_NUMBER, number);

        setResult(RESULT_OK, data);
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
                saveRentEdit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
