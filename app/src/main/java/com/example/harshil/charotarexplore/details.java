package com.example.harshil.charotarexplore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class details extends AppCompatActivity {
    location_details location_details = new location_details();
    private String name, number, address, time, lat, lon, image;
    private ImageView rimage;
    private TextView call, direction, timing, add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");
        address = getIntent().getStringExtra("address");
        time = getIntent().getStringExtra("time");
        lat = getIntent().getStringExtra("lat");
        lon = getIntent().getStringExtra("lon");
        image = getIntent().getStringExtra("image");

        getSupportActionBar().setTitle(name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(details.this, home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        rimage = (ImageView) findViewById(R.id.rimage);
        call = (TextView) findViewById(R.id.call);
        direction = (TextView) findViewById(R.id.direction);
        timing = (TextView) findViewById(R.id.timing);
        add = (TextView) findViewById(R.id.add);

        Glide.with(details.this).load(image).into(rimage);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        });
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + location_details.getLatitude() + "," + location_details.getLongitude() + "&daddr=" + lat + "," + lon));
                startActivity(intent);
            }
        });
        timing.setText(time);
        add.setText(address);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
