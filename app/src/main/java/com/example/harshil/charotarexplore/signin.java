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

public class signin extends AppCompatActivity {
    private EditText mobile, passwd;
    private Button signin;
    private TextView toUp;
    private AlertDialog exitDialog;

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
                    Toast.makeText(signin.this, "Under construction.", Toast.LENGTH_SHORT).show();
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
}
