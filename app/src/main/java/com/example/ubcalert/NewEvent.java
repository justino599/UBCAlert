package com.example.ubcalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class NewEvent extends AppCompatActivity {
    Spinner spinner;
    EditText e1;
    EditText e2;
    EditText e3;
    String title;
    String desc;
    String loc;
    String cat;



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

        e1=(EditText) findViewById(R.id.edit1);
        e2=(EditText) findViewById(R.id.edit2);
        e3=(EditText) findViewById(R.id.edit3);
         title= e1.getText().toString();
         desc=e2.getText().toString();
         loc=e3.getText().toString();
        cat=spinner.getSelectedItem().toString();

    }
    public void onClickStart(View view){

       if(!title.matches("") && !desc.matches("") && !(loc.matches(""))){
           Intent intent=new Intent(this,ExistingEvents.class);
            Bundle bundle =new Bundle();
           bundle.putString("title",title);
            bundle.putString("desc",desc);
            bundle.putString("loc",loc);
            bundle.putString("cat",cat);

            intent.putExtras(bundle);
            startActivity(intent);
        }
        else{

        }
    }

}