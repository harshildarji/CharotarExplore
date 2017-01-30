package com.example.harshil.charotarexplore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class category extends AppCompatActivity {
    data data = new data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onCatClick(View view) {
        TextView village = (TextView) view;
        data.setCid(village.getTag().toString());
        //startActivity(new Intent(category.this, results.class));
        Toast.makeText(category.this, data.getVid() + " | " + data.getCid(), Toast.LENGTH_SHORT).show();
    }

}
