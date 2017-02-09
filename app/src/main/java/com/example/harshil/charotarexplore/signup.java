package com.example.harshil.charotarexplore;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {
    private static final Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,16}");
    cached cached = new cached();
    private EditText name, mobile, passwd, cpasswd;
    private Button signup;
    private TextView toIn;
    private AlertDialog exitDialog;
    private String user_name, contact_number, password, country_code;
    private ProgressDialog signingup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = (EditText) findViewById(R.id.name);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getError() != null) {
                    name.setError(null);
                }
            }
        });
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
        passwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwd.getError() != null) {
                    passwd.setError(null);
                }
            }
        });
        cpasswd = (EditText) findViewById(R.id.cpasswd);
        cpasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cpasswd.getError() != null) {
                    cpasswd.setError(null);
                }
            }
        });
        signup = (Button) findViewById(R.id.signup);
        toIn = (TextView) findViewById(R.id.toIn);

        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        cpasswd.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    inputMethodManager.toggleSoftInput(0, 0);
                }
                return false;
            }
        });

        signingup = new ProgressDialog(signup.this);
        signingup.setTitle("Signup");
        signingup.setMessage("Registration in progress...");
        signingup.setCancelable(false);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")) {
                    name.setError("Name required.");
                } else if (mobile.getText().toString().equals("")) {
                    mobile.setError("Mobile number required.");
                } else if (mobile.getText().toString().length() != 10) {
                    mobile.setError("10 digit number required.");
                } else if (passwd.getText().toString().equals("")) {
                    passwd.setError("Password required.");
                } else if (!isPasswordValid(passwd.getText().toString())) {
                    Toast.makeText(signup.this, "Password must be 8-16 characters long with at least 1 uppercase, 1 lowercase, 1 number and 1 special character in it.", Toast.LENGTH_LONG).show();
                } else if (!isPasswordMatching(passwd.getText().toString(), cpasswd.getText().toString())) {
                    cpasswd.setError("Password does not match.");
                } else {
                    user_name = name.getText().toString();
                    contact_number = mobile.getText().toString();
                    password = passwd.getText().toString();
                    country_code = getCountryCode();
                    signupapi();
                }
            }
        });

        toIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup.this, signin.class));
            }
        });

        final AlertDialog.Builder exit = new AlertDialog.Builder(signup.this);
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

    private boolean isPasswordValid(CharSequence s) {
        return pattern.matcher(s).matches();
    }

    public boolean isPasswordMatching(String password, String confirmPassword) {
        Pattern pattern = Pattern.compile(password, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(confirmPassword);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    private String getCountryCode() {
        String country;
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        country = telephonyManager.getSimCountryIso().toUpperCase();
        String[] codes = getResources().getStringArray(R.array.country_codes);
        for (int i = 0; i < codes.length; i++) {
            String[] c = codes[i].split(",");
            if (c[1].trim().equals(country)) {
                return c[0];
            }
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        exitDialog.show();
    }

    private void signupapi() {
        signingup.show();
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.link) + "signup";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                signingup.dismiss();
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getString("status").equals("1")) {
                        JSONObject user_details = result.getJSONObject("user_details");
                        cached.setUser_id(user_details.getString("user_id"), getApplicationContext());
                        cached.setUser_name(user_details.getString("name"), getApplicationContext());
                        cached.setCountry_code(user_details.getString("country_code"), getApplicationContext());
                        cached.setContact_number(user_details.getString("contact_number"), getApplicationContext());
                        cached.setUser_avatar(user_details.getString("avatar"), getApplicationContext());
                        startActivity(new Intent(signup.this, home.class));
                    } else {
                        Toast.makeText(signup.this, result.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                signingup.dismiss();
                Toast.makeText(signup.this, "Something is wrong.", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("name", user_name);
                MyData.put("country", country_code);
                MyData.put("number", contact_number);
                MyData.put("password", password);
                return MyData;
            }
        };
        requestQueue.add(MyStringRequest);
    }
}
