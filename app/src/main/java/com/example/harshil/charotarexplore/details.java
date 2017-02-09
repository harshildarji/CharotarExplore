package com.example.harshil.charotarexplore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

public class details extends AppCompatActivity {
    cached cached = new cached();
    location_details location_details = new location_details();
    private MediaPlayer mediaPlayer;
    private ProgressDialog favorite;
    private String id, name, number, address, time, lat, lon, image;
    private ImageView rimage, fav;
    private TextView call, direction, timing, add;
    ColorFilter white = new LightingColorFilter(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"));
    ColorFilter red = new LightingColorFilter(Color.parseColor("#ff0000"), Color.parseColor("#ff0000"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mediaPlayer = MediaPlayer.create(this, R.raw.favorite);
        favorite = new ProgressDialog(this);
        favorite.setTitle("Favorite");

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");
        number = number.trim().replace(" ", "");
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
        fav = (ImageView) findViewById(R.id.fav);
        if (result.from.equals("home"))
            fav.setColorFilter(red);
        else
            fav.setColorFilter(white);
        call = (TextView) findViewById(R.id.call);
        direction = (TextView) findViewById(R.id.direction);
        timing = (TextView) findViewById(R.id.timing);
        add = (TextView) findViewById(R.id.add);

        Glide.with(details.this).load(image).into(rimage);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fav.getColorFilter() == red) {
                    fav.setColorFilter(white);
                    favorite.setMessage("Removing from favorites...");
                    favorite.show();
                    removefavapi();
                } else if (fav.getColorFilter() == white) {
                    mediaPlayer.start();
                    fav.setColorFilter(red);
                    favorite.setMessage("Adding to favorites...");
                    favorite.show();
                    addtofavapi();
                }
            }
        });
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
        timing.setText(time.trim());
        add.setText(address.trim());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toBack();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        toBack();
    }

    public void toBack() {
        Intent intent = new Intent(details.this, result.class);
        if (result.from.equals("home"))
            intent.putExtra("from", "home");
        else if (result.from.equals("category"))
            intent.putExtra("from", "category");
        startActivity(intent);
    }

    public void addtofavapi() {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.link) + "addTofav";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(details.this, "Added to favourites.", Toast.LENGTH_SHORT).show();
                favorite.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                favorite.dismiss();
                Toast.makeText(details.this, "Something is wrong.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("uid", cached.getUser_id(getApplicationContext()));
                MyData.put("rid", id);
                return MyData;
            }
        };
        requestQueue.add(MyStringRequest);
    }

    public void removefavapi() {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.link) + "deleteFav";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(details.this, "Removed from favourites.", Toast.LENGTH_SHORT).show();
                favorite.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                favorite.dismiss();
                Toast.makeText(details.this, "Something is wrong.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("uid", cached.getUser_id(getApplicationContext()));
                MyData.put("rid", id);
                return MyData;
            }
        };
        requestQueue.add(MyStringRequest);
    }
}
