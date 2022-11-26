package com.example.ubcalert;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ubcalert.databinding.ActivityPinsMapViewBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PinsMapView extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityPinsMapViewBinding binding;
    private ArrayList<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPinsMapViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventList = (ArrayList<Event>) getIntent().getSerializableExtra("eventList");
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
        mMap = googleMap;
        LatLng kelowna = new LatLng(49.936038, -119.397);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kelowna,15));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        for (Event event : eventList) {
            LatLng pin = new LatLng(event.getLat(), event.getLng());
            mMap.addMarker(new MarkerOptions().position(pin).title(event.getTitle() + " at " + event.getLocation()));
        }
    }
}