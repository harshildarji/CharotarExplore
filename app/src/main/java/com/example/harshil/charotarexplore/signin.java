package com.example.harshil.charotarexplore;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class signin extends AppCompatActivity {
    cached cached = new cached();
    private EditText mobile, passwd;
    private Button signin;
    private TextView toUp;
    private AlertDialog exitDialog;
    private String contact_number, password;
    private ProgressDialog signingin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mobile = (EditText) findViewById(R.id.mobile);
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getError() != null) {
                    mobile.setError(null);
                }
            }
        });
        passwd = (EditText) findViewById(R.id.passwd);
        signin = (Button) findViewById(R.id.signin);
        passwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwd.getError() != null) {
                    passwd.setError(null);
                }
            }
        });
        toUp = (TextView) findViewById(R.id.toUp);

        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        passwd.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    inputMethodManager.toggleSoftInput(0, 0);
                }
                return false;
            }
        });

        signingin = new ProgressDialog(signin.this);
        signingin.setTitle("Signin");
        signingin.setMessage("Authentication in progress...");
        signingin.setCancelable(false);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getText().toString().equals("")) {
                    mobile.setError("Mobile number required.");
                } else if (mobile.getText().toString().length() != 10) {
                    mobile.setError("10 digit number required.");
                } else if (passwd.getText().toString().equals("")) {
                    passwd.setError("Password required.");
                } else {
                    contact_number = mobile.getText().toString();
                    password = passwd.getText().toString();
                    signinapi();
                }
            }
        });

        toUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signin.this, signup.class));
            }
        });

        final AlertDialog.Builder exit = new AlertDialog.Builder(signin.this);
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
        exitDialog.show();
    }

    private void signinapi() {
        signingin.show();
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.link) + "signin";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                signingin.dismiss();
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getString("status").equals("1")) {
                        JSONObject user_details = result.getJSONObject("user_details");
                        cached.setUser_id(user_details.getString("user_id"), getApplicationContext());
                        cached.setUser_name(user_details.getString("name"), getApplicationContext());
                        cached.setCountry_code(user_details.getString("country_code"), getApplicationContext());
                        cached.setContact_number(user_details.getString("contact_number"), getApplicationContext());
                        cached.setUser_avatar(user_details.getString("avatar"), getApplicationContext());
                        startActivity(new Intent(signin.this, home.class));
                    } else {
                        Toast.makeText(signin.this, result.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                signingin.dismiss();
                Toast.makeText(signin.this, "Something is wrong.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("number", contact_number);
                MyData.put("password", password);
                return MyData;
            }
        };
        requestQueue.add(MyStringRequest);
    }
}
