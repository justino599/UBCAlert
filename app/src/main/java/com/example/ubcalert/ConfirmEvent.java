package com.example.ubcalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.SavedDatasetsInfo;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.threeten.bp.LocalDateTime;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class ConfirmEvent extends AppCompatActivity {
    private ArrayList<Event> eventList;
    EditText e1, e2, e3, e4;
    private String newTitle, newDesc, newLocation, newCategory, Username;

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
        Username = bundle.getString("Username");

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
    public void onClickEdit(View view){
        Intent intent = new Intent(this, EditEvent.class);
        Bundle bundle = new Bundle();
        bundle.putString("Title", e1.getText().toString());
        bundle.putString("Desc", e2.getText().toString());
        bundle.putString("Location", e3.getText().toString());
        bundle.putString("Category", e4.getText().toString());
        bundle.putString("Username", Username);
        intent.putExtras(bundle);
        startActivity(intent);

    }
    public void onClickConfirm(View view){
        // Add the values to the array list
        loadData();
        Random random = new Random();
        eventList.add(new Event(newTitle, newLocation, Username, newDesc, 49.939478 + 10*random.nextDouble(), -119.396524 + 10*random.nextDouble(), LocalDateTime.now()));
        saveData();
        Toast.makeText(this, "Event Added.", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);

    }
    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(openFileOutput("eventList.dat", MODE_PRIVATE))) {
            out.writeObject(eventList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(openFileInput("eventList.dat"))) {
            eventList = (ArrayList<Event>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}