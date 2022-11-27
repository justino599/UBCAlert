package com.example.ubcalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

public class Analytics extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner s1, s2;
    String val1, val2;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

//        ImageButton backbutton = (ImageButton) findViewById(R.id.imageButton);

        s1 = (Spinner) findViewById(R.id.planets_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.location_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(this);

        s2 = (Spinner) findViewById(R.id.planets_spinner2);
        ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource(this, R.array.graph_array, android.R.layout.simple_spinner_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adp);
        s2.setOnItemSelectedListener(this);

        b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change imageview values
                ImageView i = findViewById(R.id.imageView);
                boolean b1 = val1. equals("Campus")||val1. equals("Academy")||val1. equals("Rutland");
                int num = 1;
                if(b1 && val2.equals("Bar Graph"))
                    num = 1;
                else if(b1 && val2.equals("Line Graph"))
                    num = 2;
                else if(b1 && val2.equals("Pie Chart"))
                    num = 3;
                else if(b1 && val2.equals("Histogram"))
                    num = 4;

                switch (num) {
                    case 1:
                        i.setImageDrawable(getDrawable(R.drawable.graph1));
                        break;
                    case 2:
                        i.setImageDrawable(getDrawable(R.drawable.graph2));
                        break;
                    case 3:
                        i.setImageDrawable(getDrawable(R.drawable.graph3));
                        break;
                    case 4:
                        i.setImageDrawable(getDrawable(R.drawable.graph4));
                        break;
                }

            }
        });

//        backbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.planets_spinner)
        {
            val1 = parent.getItemAtPosition(position).toString();
        }
        else if(spinner.getId() == R.id.planets_spinner2)
        {
            val2 = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}