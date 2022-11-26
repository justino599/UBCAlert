package com.example.ubcalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {

    String useruser;
    EditText etuno;
    Button buttonbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buttonbutton = (Button) findViewById(R.id.button);
        etuno = (EditText) findViewById(R.id.editTextTextPersonName);

        buttonbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                useruser = etuno.getText().toString();
                Intent intentional = new Intent(login.this,MainActivity.class);
                intentional.putExtra("theusername", useruser);
                startActivity(intentional);
            }

        });

    }
}