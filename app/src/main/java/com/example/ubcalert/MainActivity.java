package com.example.ubcalert;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import org.threeten.bp.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements EventClickListener {
    private ArrayList<Event> eventList;
    private RecyclerView recyclerView;
    private String searchText = "";
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        username = intent.getStringExtra("theusername");
        String file = "username.txt";

        if (username == null) {
            // This means that the back button is pressed. Read the username from the file
            try {
                FileInputStream fis = openFileInput(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                username = br.readLine();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Save the username in a file.
            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(file, Context.MODE_PRIVATE);
                outputStream.write(username.getBytes());    //FileOutputStream is meant for writing streams of raw bytes.
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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

    /**
     * Loads in default events if the database has nothing in it
     **/
    private void loadDefaultData() {
        eventList = new ArrayList<>();
        eventList.add(new Event("Tim's is closed", "Tims", "admin", "Default Event", 49.939857, -119.395875, LocalDateTime.now()));
        eventList.add(new Event("Tim's out of BLT", "Tims", "admin", "Default Event", 49.939857, -119.395875, LocalDateTime.now()));
        eventList.add(new Event("Food truck", "UNC", "dale", "Default Event", 49.940821, -119.395912, LocalDateTime.now()));
        eventList.add(new Event("Car break in", "Academy", "tate", "Default Event", 49.933968, -119.401920, LocalDateTime.now()));
        eventList.add(new Event("Commons main door", "Commons", "alex", "Default Event", 49.93956349839961, -119.3955305101444, LocalDateTime.now()));
        eventList.add(new Event("SUO Free goodies", "UNC", "SUO", "Default Event", 49.940821, -119.395912, LocalDateTime.now()));
        eventList.add(new Event("AGM is happening", "UBC theatre", "SUO", "Default Event", 49.93904199035473, -119.39569629642908, LocalDateTime.now()));
        saveData();
        Log.i("LOADED DEFAULT DATA", "LOADED DEFAULT DATA");
    }

    /**
     * Set the adapter of the recyclerView
     **/
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

    /**
     * Called when the hamburger menu is clicked
     * How to use PowerMenu: https://github.com/skydoves/PowerMenu#usage
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
                Intent intentional2 = new Intent(MainActivity.this, MyReports.class);
                intentional2.putExtra("theusername", this.username);
                startActivity(intentional2);
            } else if (item.getTitle().equals("Preferences")) {
                makeSnackbar("Preferences");
            } else if (item.getTitle().equals("Event Analytics")) {
                Intent intentional3 = new Intent(MainActivity.this, Analytics.class);
                makeSnackbar("Event Analytics");
                startActivity(intentional3);
            }
        });

        powerMenu.showAsDropDown(v);
    }

    /**
     * Used for creating an in-app notification. Use instead of {@link android.widget.Toast#makeText(android.content.Context, CharSequence, int) Toast.makeText()}.
     *
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

    /**
     * Called when the "New Report" button is clicked
     **/
    public void newReport(View v) {
        makeSnackbar("New Report created");
        Intent intent = new Intent(this, NewEvent.class);
        intent.putExtra("username", username);
        startActivity(intent);

    }

    /**
     * Called when the "Open Map View" button is clicked
     **/
    public void openMapView(View v) {
        Intent intent = new Intent(this, PinsMapView.class);
        intent.putExtra("eventList", eventList);
        startActivity(intent);
    }

    @Override
    public void onShareClick(EventAdapter.EventViewHolder holder, MyUUID eventUUID) {
        Event event = findEvent(eventUUID);

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

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Report");
        alert.setMessage("Is \"" + event.getTitle() + "\" is happening?");
        alert.setPositiveButton("Yes", (dialog, which) -> {
            int hist = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                hist = event.upvoteTracking.getOrDefault(username, 0);
            }
            if (hist == 0) {
                event.upvote();
                makeSnackbar("\"" + event.getTitle() + "\" upvoted");
                event.upvoteTracking.put(username, 1);
            } else if (hist == 1) {
                makeSnackbar("You have already upvoted this. Upvote removed");
                event.upvoteTracking.put(username, 0);
                event.setNumUpvotes(event.getNumUpvotes() - 1);
            } else if (hist == -1) {
                event.upvoteTracking.put(username, 1);
                event.upvote();
                event.setNumDownvotes(event.getNumDownvotes() - 1);


                makeSnackbar("You have already downvoted this. Upvoted instead");
            }
            saveData();
            setAdapter();
        });
        alert.setNegativeButton("No", (dialog, which) -> {
            int hist = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                hist = event.upvoteTracking.getOrDefault(username, 0);
            }
            if (hist == 0) {
                event.downvote();
                makeSnackbar("\"" + event.getTitle() + "\" downvoted");
                event.upvoteTracking.put(username, -1);
            } else if (hist == -1) {
                makeSnackbar("You have already downvoted this. Downvote removed");
                event.upvoteTracking.put(username, 0);
                event.setNumDownvotes(event.getNumDownvotes() - 1);
            } else if (hist == 1) {
                event.upvoteTracking.put(username, -1);
                event.downvote();
                event.setNumUpvotes(event.getNumUpvotes() - 1);
                makeSnackbar("You have already upvoted this. Downvoted instead");
            }
            saveData();
            setAdapter();
        });
        alert.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        alert.show();
    }

    @Override
    public void onEventClick(EventAdapter.EventViewHolder holder, MyUUID eventUUID) {
        Event event = findEvent(eventUUID);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(event.getTitle());
        alert.setMessage("Location: " + event.getLocation() + "\nTime: " + DateTimeFormatter.ofPattern("hh:mm a EEE MMM dd, yyyy").format(event.timeCreatedGetter()) + "\nDescription: " + event.getDesc());
        alert.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        alert.show();
    }
}