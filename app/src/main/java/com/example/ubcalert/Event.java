package com.example.ubcalert;


import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Event implements Serializable {
    private String title, location;
    private double lat, lng;
    private int numUpvotes, numDownvotes;
    private LocalDateTime timeCreated;

    // DO NOT DELETE. USED TO APPEASE THE FIREBASE OVERLORDS
    public Event() {}

    public Event(String title, String location, double lat, double lng, LocalDateTime localDateTime) {
        this.title = title;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.timeCreated = localDateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getNumUpvotes() {
        return numUpvotes;
    }

    public void setNumUpvotes(int numUpvotes) {
        this.numUpvotes = numUpvotes;
    }

    public int getNumDownvotes() {
        return numDownvotes;
    }

    public void setNumDownvotes(int numDownvotes) {
        this.numDownvotes = numDownvotes;
    }

    /////////////////////////////////////////////////////////////////
    // timeCreated getter/setter to appease the firebase overlords //
    /////////////////////////////////////////////////////////////////
    public long getTimeCreated() {
        return timeCreated.toEpochSecond(ZoneOffset.ofHours(-8));
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = LocalDateTime.ofEpochSecond(timeCreated, 0, ZoneOffset.ofHours(-8));
    }

    ////////////////////////////////////////////////////////////
    // timeCreated getter/setter that you should actually use //
    ////////////////////////////////////////////////////////////
    public void timeCreatedSetter(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public LocalDateTime timeCreatedGetter() {
        return timeCreated;
    }
}
