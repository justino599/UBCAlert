package com.example.ubcalert;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import org.threeten.bp.LocalDateTime;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements EventClickListener {
    private ArrayList<Event> eventList;
    private RecyclerView recyclerView;
    private String searchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get views
        Button reportButton = findViewById(R.id.reportButton);
        Button openMapButton = findViewById(R.id.openMapButton);
        ImageButton menuButton = findViewById(R.id.menuButton);
        recyclerView = findViewById(R.id.recyclerView);
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                setAdapter();
                return true;
            }
        });

        // Set OnClickListeners of buttons
        reportButton.setOnClickListener(this::newReport);
        openMapButton.setOnClickListener(this::openMapView);
        menuButton.setOnClickListener(this::openMenu);

        // Load data from file
        loadData();

        // Update RecyclerView
        setAdapter();
    }

    /** Loads in default events if the database has nothing in it **/
    private void loadDefaultData() {
        eventList = new ArrayList<>();
        eventList.add(new Event("Tims is closed", "Tims", 49.939857, -119.395875, LocalDateTime.now()));
        eventList.add(new Event("Food truck", "UNC", 49.940821, -119.395912, LocalDateTime.now()));
        eventList.add(new Event("Car break in", "Academy", 49.933968, -119.401920, LocalDateTime.now()));
        saveData();
        Log.i("LOADED DEFAULT DATA","LOADED DEFAULT DATA");
    }

    /** Set the adapter of the recyclerView **/
    private void setAdapter() {
        ArrayList<Event> filtered = getFilteredEventList();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), filtered);
        eventAdapter.setEventClickListener(this);
        recyclerView.setAdapter(eventAdapter);
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

    private ArrayList<Event> getFilteredEventList() {
        ArrayList<Event> out = new ArrayList<>();

        for (Event event : eventList) {
            if (!searchText.equals("") && !event.getTitle().toLowerCase(Locale.ROOT).contains(searchText.toLowerCase(Locale.ROOT)) && !event.getLocation().toLowerCase(Locale.ROOT).contains(searchText.toLowerCase(Locale.ROOT))) {
                continue;
            }

            out.add(event);
        }

        return out;
    }

    /** Called when the hamburger menu is clicked
     *  How to use PowerMenu: https://github.com/skydoves/PowerMenu#usage
     **/
    public void openMenu(View v) {
        PowerMenu powerMenu = new PowerMenu.Builder(this)
                .addItem(new PowerMenuItem("My Reports", false))
                .addItem(new PowerMenuItem("Preferences", false))
                .addItem(new PowerMenuItem("Event Analytics", false))
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | RIGHT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(this, R.color.black))
                .setTextGravity(Gravity.CENTER)
                .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(this, R.color.light_grey_1))
                .build();

        powerMenu.setOnMenuItemClickListener((position, item) -> {
            // Automatically close the menu once an item is clicked
            powerMenu.dismiss();

            // Determine which item was clicked and open the corresponding activity
            if (item.getTitle().equals("My Reports")) {
                makeSnackbar("My Reports");
            } else if (item.getTitle().equals("Preferences")) {
                makeSnackbar("Preferences");
            } else if (item.getTitle().equals("Event Analytics")) {
                makeSnackbar("Event Analytics");
            }
        });

        powerMenu.showAsDropDown(v);
    }

    /** Used for creating an in-app notification. Use instead of {@link android.widget.Toast#makeText(android.content.Context, CharSequence, int) Toast.makeText()}.
     * @param message The message to display in the notification
     */
    private void makeSnackbar(String message) {
        Snackbar.make(findViewById(R.id.coordinatorLayout), message, Snackbar.LENGTH_LONG).show();
    }

    private Event findEvent(MyUUID uuid) {
        for (int i = 0; i < eventList.size(); i++)
            if (eventList.get(i).getUuid().equals(uuid))
                return eventList.get(i);
        return null;
    }

    /** Called when the "New Report" button is clicked **/
    public void newReport(View v) {
        makeSnackbar("New Report created");
        Intent intent=new Intent(this, NewEvent.class);


        startActivity(intent);

    }

    /** Called when the "Open Map View" button is clicked **/
    public void openMapView(View v) {
        makeSnackbar("Map View opened");
    }

    @Override
    public void onShareClick(EventAdapter.EventViewHolder holder, MyUUID eventUUID) {
        Event event = findEvent(eventUUID);
        makeSnackbar("Share button clicked on \"" + (event != null ? event.getTitle() : null) + "\"");

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, event.getTitle() + " at " + event.getLocation());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    public void onReportClick(EventAdapter.EventViewHolder holder, MyUUID eventUUID) {
        Event event = findEvent(eventUUID);
        makeSnackbar("Report button clicked on \"" + (event != null ? event.getTitle() : null) + "\"");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Report");
        alert.setMessage("Is \"" + event.getTitle() + "\" is happening?");
        alert.setPositiveButton("Yes", (dialog, which) -> {
            event.upvote();
            saveData();
            System.out.println(eventList);
        });
        alert.setNegativeButton("No", (dialog, which) -> {
            event.downvote();
            saveData();
            setAdapter();
            System.out.println(eventList);
        });
        alert.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        alert.show();
    }
}