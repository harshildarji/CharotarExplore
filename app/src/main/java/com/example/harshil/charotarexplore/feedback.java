package com.example.harshil.charotarexplore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class feedback extends AppCompatActivity {
    private EditText subject, message;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Feedback");

        subject = (EditText) findViewById(R.id.subject);
        message = (EditText) findViewById(R.id.message);
        message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (message.getError() != null) {
                    message.setError(null);
                }
                return false;
            }
        });
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("plain/text");
                intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"charotarexplore@gmail.com"});
                if (!subject.getText().toString().trim().equals(""))
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
                if (message.getText().toString().trim().equals("")) {
                    message.setError("Message required.");
                } else {
                    intent.putExtra(Intent.EXTRA_TEXT, message.getText().toString());
                    startActivity(Intent.createChooser(intent, "Feedback"));
                }
            }
        });
    }
}
