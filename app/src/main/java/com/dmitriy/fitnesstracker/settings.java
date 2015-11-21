package com.dmitriy.fitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Dmytro Vertepniy on 28.10.2015.
 */
public class settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }

    public void fabClick(View v){
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);

    }

}
