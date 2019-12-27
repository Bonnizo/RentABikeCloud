package com.example.rentabike.ui.Bike;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rentabike.R;
import com.example.rentabike.database.entity.BikeEntity;
import com.example.rentabike.ui.BikeActivity;
import com.example.rentabike.ui.MainActivity;
import com.example.rentabike.viewmodel.BikeViewModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wafflecopter.charcounttextview.CharCountTextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Class AddBikeActivity qui permet de rajouter un nouveau bike.
 * On doit remplir les données du formulaire et ensuite il y a la possibilité de sauvegarder
 * Renvoie les données à la page BikeActivity qui exécutera l'action de rajouter le bike dans notre base de données
 */

public class AddBikeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    public static final int PICK_IMAGE_REQUEST = 3;
    /* public static final String EXTRA_NAME =
             "com.example.rentabike.ui.Bike.EXTRA_NAME";
     public static final String EXTRA_DESCRIPTION =
             "com.example.rentabike.ui.Bike.EXTRA_DESCRIPTION";
     public static final String EXTRA_SIZE =
             "com.example.rentabike.ui.Bike.EXTRA_SIZE";

     public static final String EXTRA_PICTURE=
             "com.example.rentabike.ui.Bike.EXTRA_PRICE";
 */
    private EditText editTextName;
    private EditText editTextDescription;
    private String editTextSize;
    private ImageView imageViewPicture;
    private Spinner spinnerSize;
    private BikeViewModel bikeViewModel;
    private String img;
    private String imgSave = ("bike" + "/" + "bikedefault" + ".png");
    private StorageReference storageReference;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bike);

        bikeViewModel = new BikeViewModel(getApplication());


        editTextName = findViewById(R.id.edit_bike_name);
        editTextDescription = findViewById(R.id.edit_bike_description);
        spinnerSize = findViewById(R.id.edit_bike_size);
        imageViewPicture = findViewById(R.id.imageViewAdd);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.bikedefault);
        imageViewPicture.setImageBitmap(bm);

        storageReference = FirebaseStorage.getInstance().getReference();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sizeAvailable, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(adapter);
        spinnerSize.setOnItemSelectedListener(this);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        setTitle("Add a new Bike");


        Button selectImage = findViewById(R.id.uploadImage);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        CharCountTextView charCountTextView = (CharCountTextView) findViewById(R.id.tvTextCounter_description);
        EditText editText = (EditText) findViewById(R.id.edit_bike_description);
        charCountTextView.setEditText(editText);
        charCountTextView.setCharCountChangedListener(new CharCountTextView.CharCountChangedListener() {
            @Override
            public void onCountChanged(int i, boolean b) {

            }
        });
    }


    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageViewPicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadFile() {

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReferenceProfilePic = firebaseStorage.getReference();

        /*name of the pic = time in millisecond*/
        img = String.valueOf(System.currentTimeMillis());


        StorageReference imageRef = storageReferenceProfilePic.child("bike" + "/" + img + ".png");
        imgSave = ("bike" + "/" + img + ".png");
        imageRef.putFile(uri);

    }

    private void saveBike() {
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String size = editTextSize;
        Bitmap bitmapTemp = ((BitmapDrawable) imageViewPicture.getDrawable()).getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapTemp.compress(Bitmap.CompressFormat.JPEG, 80, stream);



        if (name.isEmpty() || description.isEmpty() || size.isEmpty()) {
            Toast.makeText(this, "Please complete all data", Toast.LENGTH_SHORT).show();
            return;
        }

        if (uri != null)
            uploadFile();


        BikeEntity bikeEntity = new BikeEntity(name, description, size, imgSave);

        bikeViewModel.insert(bikeEntity);

        Toast.makeText(this, "Bike added", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AddBikeActivity.this, BikeActivity.class);
        startActivity(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bike_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_bike:
                saveBike();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        editTextSize = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
