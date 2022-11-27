package com.example.ubcalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditEvent extends AppCompatActivity {
    EditText e1;
    EditText e2;
    EditText e3;
    Spinner s;
    private String newTitle;
    private String newDesc;
    private String newLocation;
    private String newCategory;
    private String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        e1 = (EditText) findViewById(R.id.edit1);
        e2 = (EditText) findViewById(R.id.edit2);
        e3 = (EditText) findViewById(R.id.edit3);
        s = (Spinner) findViewById(R.id.spinner2);
        // Create an ArrayAdapter using the string array and a default
        // spinner layout
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.cat, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        s.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        newTitle = bundle.getString("Title");
        newDesc = bundle.getString("Desc");
        newLocation = bundle.getString("Location");
        newCategory = bundle.getString("Category");
        Username = bundle.getString("Username");

        e1.setText(newTitle);
        e2.setText(newDesc);
        e3.setText(newLocation);
        String compareValue = newCategory;
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.cat, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            s.setSelection(spinnerPosition);
        }


    }

    public void onClickCancel(View view) {
        finish();
    }

    public void onClickConfirm(View view) {

        if (!e1.getText().toString().matches("") && !e2.getText().toString().matches("") && !e3.getText().toString().matches("")) {
            Intent intent = new Intent(this, ConfirmEvent.class);
            Bundle bundle = new Bundle();
            bundle.putString("Title", e1.getText().toString());
            bundle.putString("Desc", e2.getText().toString());
            bundle.putString("Location", e3.getText().toString());
            bundle.putString("Category", s.getSelectedItem().toString().substring(0, 1));
            bundle.putString("Username", Username);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            if (e1.getText().toString().matches("")) {
                Toast.makeText(this, "Please fill the Title ", Toast.LENGTH_LONG).show();
            } else if (e2.getText().toString().matches("")) {
                Toast.makeText(this, "Please fill the Description", Toast.LENGTH_LONG).show();
            } else if (e3.getText().toString().matches("")) {
                Toast.makeText(this, "Please fill the Location", Toast.LENGTH_LONG).show();
            }
        }

    }
}