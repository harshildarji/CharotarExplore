package com.example.harshil.charotarexplore;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Dialog.OnCancelListener {
    cached cached = new cached();
    data data = new data();
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_INTENT = 2;
    private AlertDialog exitDialog, outDialog, imageMethod;
    private ProgressDialog uploading;
    private Bitmap bitmap = null;
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
        displayAvatar();

        uploading = new ProgressDialog(home.this);
        uploading.setCancelable(false);
        uploading.setTitle("Avatar upload");
        uploading.setMessage("Avatar upload in progress...");
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageMethod.show();
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

        AlertDialog.Builder method = new AlertDialog.Builder(home.this);
        method.setMessage("Please select method:");
        method.setPositiveButton("Use Gallery",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imageMethod.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, GALLERY_INTENT);
                    }
                });
        method.setNegativeButton("Use Camera",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imageMethod.dismiss();
                        captureImage();
                    }
                });
        imageMethod = method.create();
    }

    public void displayAvatar() {
        Log.e("IMAGE_LINK", cached.getUser_avatar(getApplicationContext()));
        Glide.with(home.this).load(cached.getUser_avatar(getApplicationContext())).asBitmap().centerCrop().error(R.drawable.face).into(new BitmapImageViewTarget(avatar) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(home.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                avatar.setImageDrawable(circularBitmapDrawable);
            }
        });
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
        } else if (id == R.id.favorite) {
            Intent intent = new Intent(home.this, result.class);
            intent.putExtra("from", "home");
            startActivity(intent);
        } else if (id == R.id.share_app) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Charotar Explore");
            String sAux = "New in CHAROTAR? or jsut want to explore CHAROTAR?\nDownload our app from:";
            sAux = sAux + "https://www.google.com/";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            navigationView.setCheckedItem(R.id.home_page);
            startActivity(Intent.createChooser(i, "Share app using:"));
        } else if (id == R.id.about) {
            final Dialog dialog = new Dialog(home.this);
            dialog.setContentView(R.layout.about);
            dialog.setCancelable(true);
            dialog.setOnCancelListener(home.this);
            final TextView contact = (TextView) dialog.findViewById(R.id.contactus);
            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + contact.getText().toString()));
                    startActivity(callIntent);
                }
            });
            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        navigationView.setCheckedItem(R.id.home_page);
    }

    public void onCatClick(View view) {
        TextView village = (TextView) view;
        data.setVid(village.getTag().toString());
        startActivity(new Intent(home.this, category.class));
    }

    public void captureImage() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        android.Manifest.permission.CAMERA
                }, 6);
            }
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 6:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    captureImage();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uploading.show();
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
        } else if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bitmap != null) {
            toStringImage(bitmap);
        } else {
            uploading.dismiss();
        }
    }

    public void toStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        uploadimageapi(encodedImage);
    }

    public void uploadimageapi(String encodedImage) {
        final String avatar = encodedImage;
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.link) + "uploadAvatar";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                uploading.dismiss();
                try {
                    if (new JSONObject(response).getString("status").equals("1"))
                        cached.setUser_avatar(new JSONObject(response).getString("avatar_link"), getApplicationContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayAvatar();
                Toast.makeText(home.this, "Avatar successfully uploaded.", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                uploading.dismiss();
                Toast.makeText(home.this, "Something is wrong.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("user_id", cached.getUser_id(getApplicationContext()));
                MyData.put("avatar", avatar);
                return MyData;
            }
        };
        requestQueue.add(MyStringRequest);
    }
}
