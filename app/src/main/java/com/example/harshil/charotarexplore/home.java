package com.example.harshil.charotarexplore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    cached cached = new cached();
    data data = new data();
    private AlertDialog exitDialog, outDialog;
    private TextView user_name;
    private ImageView avatar;
    private Button signout;
    private NavigationView navigationView;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home_page);

        final AlertDialog.Builder out = new AlertDialog.Builder(home.this);
        out.setTitle("Signout");
        out.setMessage("Are you sure you want to Signout?");
        out.setCancelable(false);
        out.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cached.clearCache(getApplicationContext());
                        Intent intent = new Intent(home.this, signin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
        out.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        outDialog.dismiss();
                        navigationView.setCheckedItem(R.id.home_page);
                    }
                });
        outDialog = out.create();

        signout = (Button) findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outDialog.show();
            }
        });

        View headerView = navigationView.getHeaderView(0);
        user_name = (TextView) headerView.findViewById(R.id.user_name);
        avatar = (ImageView) headerView.findViewById(R.id.avatar);

        user_name.setText(cached.getUser_name(getApplicationContext()));
        Glide.with(home.this).load(cached.getUser_avatar(getApplicationContext())).asBitmap().centerCrop().error(R.drawable.face).into(new BitmapImageViewTarget(avatar) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(home.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                avatar.setImageDrawable(circularBitmapDrawable);
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(home.this, "Avatar upload.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog.Builder exit = new AlertDialog.Builder(home.this);
        exit.setTitle("Exit");
        exit.setMessage("Are you sure you want to exit?");
        exit.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                }).setCancelable(false);
        exit.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitDialog.dismiss();
                    }
                }).setCancelable(true);
        exitDialog = exit.create();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exitDialog.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawer.closeDrawers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.home_page);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home_page) {
            // Handle the camera action
        } else if (id == R.id.share_app) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Charotar Explore");
            String sAux = "New in CHAROTAR? or jsut want to explore CHAROTAR?\nDownload our app from:";
            sAux = sAux + "https://www.google.com/";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            navigationView.setCheckedItem(R.id.home_page);
            startActivity(Intent.createChooser(i, "Share app using:"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onCatClick(View view) {
        TextView village = (TextView) view;
        data.setVid(village.getTag().toString());
        startActivity(new Intent(home.this, category.class));
    }
}
