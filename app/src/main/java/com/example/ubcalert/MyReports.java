package com.example.ubcalert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.LocalDateTime;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MyReports extends AppCompatActivity implements EventClickListener {
    private ArrayList<Event> eventList;
    String username;
    private RecyclerView recycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        Intent intent = getIntent();
        username = intent.getStringExtra("theusername");
        recycler = findViewById(R.id.recyclerwoo);
        loadData();
        setAdapter();
    }

    private void setAdapter() {
        ArrayList<Event> filtered = getFilteredEventList();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), filtered);
        eventAdapter.setEventClickListener(this);
        recycler.setAdapter(eventAdapter);
    }

    private ArrayList<Event> getFilteredEventList() {
        ArrayList<Event> out = new ArrayList<>();

        for (Event event : eventList) {
            System.out.println(event.getUsername());
            System.out.println(this.username);
            if (event.getUsername().equals(this.username)) {
                out.add(event);
            }

        }
        System.out.println(out);
        return out;
    }

    private void loadDefaultData() {
        eventList = new ArrayList<>();
        eventList.add(new Event("Tims is closed", "Tims", "jade", "Default Event", 49.939857, -119.395875, LocalDateTime.now()));
        eventList.add(new Event("Food truck", "UNC", "jade", "Default Event", 49.940821, -119.395912, LocalDateTime.now()));
        eventList.add(new Event("Car break in", "Academy","shiv", "Default Event", 49.933968, -119.401920, LocalDateTime.now()));
        saveData();
        Log.i("LOADED DEFAULT DATA","LOADED DEFAULT DATA");
    }

    private void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(openFileInput("eventList.dat"))) {
            eventList = (ArrayList<Event>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            loadDefaultData();
        }
    }

    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(openFileOutput("eventList.dat", MODE_PRIVATE))) {
            out.writeObject(eventList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShareClick(EventAdapter.EventViewHolder holder, MyUUID eventUUID) {

    }

    @Override
    public void onReportClick(EventAdapter.EventViewHolder holder, MyUUID eventUUID) {

    }

    @Override
    public void onEventClick(EventAdapter.EventViewHolder holder, MyUUID eventUUID) {

    }
}