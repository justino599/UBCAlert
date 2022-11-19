package com.example.ubcalert;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EventClickListener {
    private ArrayList<Event> eventList;
    private RecyclerView recyclerView;
    private DatabaseReference eventDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get views
        Button reportButton = findViewById(R.id.reportButton);
        Button openMapButton = findViewById(R.id.openMapButton);
        ImageButton menuButton = findViewById(R.id.menuButton);
        recyclerView = findViewById(R.id.recyclerView);

        // Set OnClickListeners of buttons
        reportButton.setOnClickListener(this::newReport);
        openMapButton.setOnClickListener(this::openMapView);
        menuButton.setOnClickListener(this::openMenu);

        // Get reference to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        eventDataRef = database.getReference("eventList");

        // Read from the database
        eventDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once on activity launch and again whenever data at this location is updated.
                try {
                    eventList = dataSnapshot.getValue(new GenericTypeIndicator<ArrayList<Event>>() {
                    });
                    assert eventList != null;
                } catch (Exception | AssertionError e) {
                    loadDefaultData();
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                loadDefaultData();
                System.out.println("Failed to read value.");
                setAdapter();
            }
        });

    }

    /** Loads in default events if the database has nothing in it **/
    private void loadDefaultData() {
        eventList = new ArrayList<>();
        eventList.add(new Event("Tims is closed", "Tims", 49.939857, -119.395875, LocalDateTime.now()));
        eventList.add(new Event("Food truck", "UNC", 49.940821, -119.395912, LocalDateTime.now()));
        eventList.add(new Event("Car break in", "Academy", 49.933968, -119.401920, LocalDateTime.now()));
        eventDataRef.setValue(eventList);
        Log.i("LOADED DEFAULT DATA","LOADED DEFAULT DATA");
    }

    /** Set the adapter of the recyclerView **/
    private void setAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), eventList);
        eventAdapter.setEventClickListener(this);
        recyclerView.setAdapter(eventAdapter);
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

    /** Called when the "New Report" button is clicked **/
    public void newReport(View v) {
        makeSnackbar("New Report created");
    }

    /** Called when the "Open Map View" button is clicked **/
    public void openMapView(View v) {
        makeSnackbar("Map View opened");
    }

    @Override
    public void onShareClick(EventAdapter.EventViewHolder holder, int position) {
        makeSnackbar("Share button clicked on \"" + eventList.get(position).getTitle() + "\"");
    }

    @Override
    public void onReportClick(EventAdapter.EventViewHolder holder, int position) {
        makeSnackbar("Report button clicked on \"" + eventList.get(position).getTitle() + "\"");
    }
}