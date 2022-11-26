package com.example.ubcalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class NewEvent extends AppCompatActivity {
    Spinner spinner;
    EditText e1;
    EditText e2;
    EditText e3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default
        // spinner layout
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.cat, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        e1 = (EditText) findViewById(R.id.edit1);
        e2 = (EditText) findViewById(R.id.edit2);
        e3 = (EditText) findViewById(R.id.edit3);

    }

    public void onClickStart(View view) {
        Intent intent = new Intent(this, ExistingEvents.class);
        Bundle bundle = new Bundle();
        bundle.putString("Title", e1.getText().toString());
        bundle.putString("Desc", e2.getText().toString());
        bundle.putString("Location", e3.getText().toString());
        bundle.putString("Category", spinner.getSelectedItem().toString().substring(0, 1));
        intent.putExtras(bundle);
        startActivity(intent);
    }

}