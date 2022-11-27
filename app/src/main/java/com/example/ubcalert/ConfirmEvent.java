package com.example.ubcalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class ConfirmEvent extends AppCompatActivity {
    EditText e1, e2, e3, e4;
    private String newTitle, newDesc, newLocation, newCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_event);

        // Retrieving all the elements from bundle
        e1 = (EditText) findViewById(R.id.edit1);
        e2 = (EditText) findViewById(R.id.edit2);
        e3 = (EditText) findViewById(R.id.edit3);
        e4 = (EditText) findViewById(R.id.edit4);

        // Retrieving the new data that is potentially to be reported
        Bundle bundle = getIntent().getExtras();
        newTitle = bundle.getString("Title");
        newDesc = bundle.getString("Desc");
        newLocation = bundle.getString("Location");
        newCategory = bundle.getString("Category");

        e1.setText(newTitle);
        e2.setText(newDesc);
        e3.setText(newLocation);
        e4.setText(newCategory);

        e1.setFocusable(false);
        e2.setFocusable(false);
        e3.setFocusable(false);
        e4.setFocusable(false);
    }

    public void onClickCancel(View view){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);

    }
}