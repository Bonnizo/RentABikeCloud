package com.example.rentabike.ui.Bike;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rentabike.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wafflecopter.charcounttextview.CharCountTextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class EditBike qui permet de modifier un bike.
 * Reçoit les données de la part de BikeActivity, récupère les données du formulaire et les renvoie à BikeActivity pour l'enregistrement et l'update
 */

public class EditBikeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final int PICK_IMAGE_REQUEST = 3;

    public static final String EDIT_EXTRA_ID =
            "com.example.rentabike.ui.Bike.EDIT_EXTRA_ID";
    public static final String EDIT_EXTRA_NAME =
            "com.example.rentabike.ui.Bike.EDIT_EXTRA_NAME";
    public static final String EDIT_EXTRA_DESCRIPTION =
            "com.example.rentabike.ui.Bike.EDIT_EXTRA_DESCRIPTION";
    public static final String EDIT_EXTRA_SIZE =
            "com.example.rentabike.ui.Bike.EDIT_EXTRA_SIZE";
    public static final String EDIT_EXTRA_PICTURE =
            "com.example.rentabike.ui.Bike.EDIT_EXTRA_PRICE";


    private String Id;
    private EditText editTextName;
    private EditText editTextDescription;
    private Spinner spinnerSize;
    private ImageView imageViewPicture;
    private String editTextSize;
    private String picture;
    private Uri uri;
    private String img;
    private String imgSave = ("bike" + "/" + "bikedefault" + ".png");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bike);


        editTextName = findViewById(R.id.edit_bike_name);
        editTextDescription = findViewById(R.id.edit_bike_description);
        spinnerSize = findViewById(R.id.edit_bike_size);
        imageViewPicture = findViewById(R.id.imageViewEdit);

        Intent intent = getIntent();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sizeAvailable, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(adapter);
        spinnerSize.setOnItemSelectedListener(this);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        setTitle("Edit a Bike");

        //passer les infos du bike
        Id = intent.getStringExtra(EDIT_EXTRA_ID);
        editTextName.setText(intent.getStringExtra(EDIT_EXTRA_NAME));
        editTextDescription.setText(intent.getStringExtra(EDIT_EXTRA_DESCRIPTION));


        picture = intent.getStringExtra(EDIT_EXTRA_PICTURE);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference().child(picture);

        String tempName = picture.substring(5, picture.length() - 4);

        try {
            final File localFile = File.createTempFile(tempName, "png");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageViewPicture.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button selectImage = findViewById(R.id.uploadImage2);
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

    private void uploadFile() {

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReferenceProfilePic = firebaseStorage.getReference();

        /*name of the pic = time in millisecond*/
        img = String.valueOf(System.currentTimeMillis());


        StorageReference imageRef = storageReferenceProfilePic.child("bike" + "/" + img + ".png");
        imgSave = ("bike" + "/" + img + ".png");
        imageRef.putFile(uri);

    }

    private void saveBikeEdit() {


        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String size = editTextSize;


        if (uri != null)
            uploadFile();

        Intent data = new Intent();
        data.putExtra(EDIT_EXTRA_ID, Id);
        data.putExtra(EDIT_EXTRA_NAME, name);
        data.putExtra(EDIT_EXTRA_DESCRIPTION, description);
        data.putExtra(EDIT_EXTRA_SIZE, size);
        data.putExtra(EDIT_EXTRA_PICTURE, imgSave);

        setResult(RESULT_OK, data);
        finish();
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
                saveBikeEdit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        editTextSize = parent.getItemAtPosition(position).toString();
    }


    public void onNothingSelected(AdapterView<?> parent) {

    }
}
