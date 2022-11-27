package com.example.ubcalert;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ubcalert.databinding.ActivityPinsMapViewBinding;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PinsMapView extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityPinsMapViewBinding binding;
    private ArrayList<Event> eventList;
    private RadioGroup radioGroup;
    private int filter = R.id.monthRadio;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPinsMapViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FragmentActivity f = this;
        eventList = (ArrayList<Event>) getIntent().getSerializableExtra("eventList");
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            System.out.println(checkedId);
            filter = checkedId;
            loadMap();
        });

        findViewById(R.id.imageButton1).setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Info");
            alert.setMessage("This map shows the location of all existing reports. Click on a pin for more information about that event. You can filter by time to only see more recent events.");
            alert.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            alert.show();
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        loadMap();
    }

    private void loadMap() {
        googleMap.clear();
        mMap = googleMap;
        LatLng kelowna = new LatLng(49.936038, -119.397);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kelowna,15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        System.out.println(eventList);
        for (Event event : eventList) {
            LocalDateTime now = LocalDateTime.now();
            if ((filter == R.id.hourRadio && Math.abs(now.toEpochSecond(ZoneOffset.UTC) - event.timeCreatedGetter().toEpochSecond(ZoneOffset.UTC)) < 3600) || (filter == R.id.todayRadio && Math.abs(now.toEpochSecond(ZoneOffset.UTC) - event.timeCreatedGetter().toEpochSecond(ZoneOffset.UTC)) < 3600 * 24) || (filter == R.id.weekRadio && Math.abs(now.toEpochSecond(ZoneOffset.UTC) - event.timeCreatedGetter().toEpochSecond(ZoneOffset.UTC)) < 3600 * 24 * 7) || (filter == R.id.monthRadio && Math.abs(now.toEpochSecond(ZoneOffset.UTC) - event.timeCreatedGetter().toEpochSecond(ZoneOffset.UTC)) < 3600 * 24 * 30) || (filter == R.id.yearRadio && Math.abs(now.toEpochSecond(ZoneOffset.UTC) - event.timeCreatedGetter().toEpochSecond(ZoneOffset.UTC)) < 3600 * 24 * 365)) {
                LatLng pin = new LatLng(event.getLat(), event.getLng());
                mMap.addMarker(new MarkerOptions().position(pin).title(event.getTitle() + " at " + event.getLocation()));
            }
        }
    }

    public void goBack(View v) {
        finish();
    }

    public void heatMap(View v) {

    }
}