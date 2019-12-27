package com.example.rentabike.ui;

import android.content.Intent;
import android.os.Bundle;
import com.example.rentabike.R;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void startApp(View view) {
        Intent intent = new Intent(MainActivity.this, BikeActivity.class);
        startActivity(intent);
    }


}
