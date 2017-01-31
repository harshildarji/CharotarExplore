package com.example.harshil.charotarexplore;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class launch extends BaseLocationActivity {
    cached cached = new cached();
    private static final String BROADCAST = "com.example.harshil.charotarexplore.android.action.broadcast";
    private ProgressBar loading;
    private LinearLayout sign;
    private AlertDialog internetDialog, locationDialog;
    private Button signin, signup;

    private ConnectivityManager connectivityManager;
    private LocationManager locationManager;
    private NetworkInfo networkInfo;
    private BroadcastReceiver broadcastReceiver, locationReceiver, networkReceiver;
    private NetworkChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        loading = (ProgressBar) findViewById(R.id.loading);
        sign = (LinearLayout) findViewById(R.id.sign);
        signin = (Button) findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(launch.this, signin.class));
            }
        });
        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(launch.this, signup.class));
            }
        });

        AlertDialog.Builder internet = new AlertDialog.Builder(launch.this);
        internet.setMessage("Internet connection required.");
        internet.setPositiveButton("SETTING",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                }).setCancelable(false);
        internetDialog = internet.create();

        AlertDialog.Builder location = new AlertDialog.Builder(launch.this);
        location.setMessage("GPS turned off.");
        location.setPositiveButton("GPS SETTING",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }).setCancelable(false);
        locationDialog = location.create();

        locationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().matches(BROADCAST)) {
                    onLocationAvailable();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationDialog.show();
            }
        } else {
            internetDialog.show();
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        locationDialog.show();
                    } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (locationDialog.isShowing()) {
                            locationDialog.dismiss();
                        }
                        launch.super.googleApiClient.connect();
                    }
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        registerReceiver(locationReceiver, new IntentFilter(BROADCAST));
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            if (internetDialog.isShowing()) {
                internetDialog.dismiss();
            }
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationDialog.show();
            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (locationDialog.isShowing()) {
                    locationDialog.dismiss();
                }
            }
        } else {
            internetDialog.show();
        }
    }

    public void onLocationAvailable() {
        if (super.location != null && String.valueOf(super.location.getLatitude()) != null && String.valueOf(super.location.getLongitude()) != null) {
            location_details.setLatitude(String.valueOf(super.location.getLatitude()));
            location_details.setLongitude(String.valueOf(super.location.getLongitude()));
            unregisterReceiver(locationReceiver);
            unregisterReceiver(receiver);
            unregisterReceiver(broadcastReceiver);
            loading.setVisibility(View.GONE);
            sign.setVisibility(View.VISIBLE);
            if (cached.getUser_id(getApplicationContext()).equals("")) {
                sign.animate().alpha(1f).setDuration(500);
            } else {
                startActivity(new Intent(launch.this, home.class));
            }
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            isNetworkAvailable(context);
        }

        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if (internetDialog.isShowing()) {
                                internetDialog.dismiss();
                                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                    locationDialog.show();
                                }
                                launch.super.googleApiClient.connect();
                            }
                        }
                    }
                }
            }
            return false;
        }
    }
}
