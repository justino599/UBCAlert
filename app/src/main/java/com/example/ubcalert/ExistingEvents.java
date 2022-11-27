package com.example.ubcalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.threeten.bp.LocalDateTime;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class ExistingEvents extends AppCompatActivity implements ExistingEventClickListener {
    private ArrayList<Event> eventList;
    private RecyclerView recyclerView;
    private String newTitle, newDesc, newLocation, newCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_events);

        // Getting elements from the page
        recyclerView = findViewById(R.id.recyclerView);

        // Loading all the events from the data file
        loadData();

        // Retrieving the new data that is potentially to be reported
        Bundle bundle = getIntent().getExtras();
        newTitle = bundle.getString("Title");
        newDesc = bundle.getString("Desc");
        newLocation = bundle.getString("Location");
        newCategory = bundle.getString("Category");

        // Update RecyclerView
        setAdapter(bundle);
    }

    private void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(openFileInput("eventList.dat"))) {
            eventList = (ArrayList<Event>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // The data should have been already loaded
            e.printStackTrace();
        }
    }

    /** Set the adapter of the recyclerView **/
    private void setAdapter(Bundle bundle) {
        ArrayList<Event> filtered = getFilteredEventList();
        if (filtered.size() == 0) {
            // Directly go the confirmation page
            Intent intent = new Intent(this, ConfirmEvent.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ExistingEventAdapter eventAdapter = new ExistingEventAdapter(getApplicationContext(), filtered);
        eventAdapter.setEventClickListener(this);
        recyclerView.setAdapter(eventAdapter);
    }

    private ArrayList<Event> getFilteredEventList() {
        ArrayList<Event> out = new ArrayList<>();

        for (Event event : eventList) {
            String[] keywords = newTitle.toLowerCase(Locale.ROOT).split(" ");
            boolean isRelevant = false;
            for (String keyword : keywords) {
                if (event.getTitle().toLowerCase(Locale.ROOT).contains(keyword)) {
                    isRelevant = true;
                    break;
                }
            }
            if (isRelevant)
                out.add(event);
        }
        return out;
    }

    @Override
    public void onReportClick(ExistingEventAdapter.EventViewHolder holder, MyUUID eventUUID) {
        Event event = findEvent(eventUUID);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Report");
        alert.setMessage("Is the event, \"" + event.getTitle() + "\" similar to your report?");

        alert.setPositiveButton("Yes", (dialog, which) -> {
            // Go the go the the homepage
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        alert.setNeutralButton("No", (dialog, which) -> dialog.dismiss());
        alert.show();
    }

    private Event findEvent(MyUUID uuid) {
        for (int i = 0; i < eventList.size(); i++)
            if (eventList.get(i).getUuid().equals(uuid))
                return eventList.get(i);
        return null;
    }
    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(openFileOutput("eventList.dat", MODE_PRIVATE))) {
            out.writeObject(eventList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}