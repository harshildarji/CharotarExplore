package com.example.harshil.charotarexplore;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {
    private EditText name, mobile, passwd, cpasswd;
    private Button signup;
    private TextView toIn;
    private AlertDialog exitDialog;

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
                } else if (!isPasswordMatching(passwd.getText().toString(), cpasswd.getText().toString())) {
                    cpasswd.setError("Password does not match.");
                } else {
                    Toast.makeText(signup.this, "Under construction.", Toast.LENGTH_SHORT).show();
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

    public boolean isPasswordMatching(String password, String confirmPassword) {
        Pattern pattern = Pattern.compile(password, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(confirmPassword);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        exitDialog.show();
    }
}
