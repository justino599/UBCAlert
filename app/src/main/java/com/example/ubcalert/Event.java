package com.example.ubcalert;


import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Event implements Serializable {
    private String title;
    private String location;
    private String username;
    private double lat, lng;
    private int numUpvotes = 1, numDownvotes;
    private LocalDateTime timeCreated;
    private MyUUID uuid;

    // DO NOT DELETE. USED TO APPEASE THE FIREBASE OVERLORDS
    public Event() {}

    public Event(String title, String location, String username, double lat, double lng, LocalDateTime localDateTime) {
        this.title = title;
        this.location = location;
        this.username = username;
        this.lat = lat;
        this.lng = lng;
        this.timeCreated = localDateTime;
        this.uuid = MyUUID.randomUUID();
    }
    public String getUsername() {return username; }

    public void setUsername(String username) {this.username = username; }

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

    public MyUUID getUuid() {
        return uuid;
    }

    public void upvote() {
        numUpvotes++;
    }

    public void downvote() {
        numDownvotes++;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return uuid != null ? uuid.equals(event.uuid) : event.uuid == null;
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", username =" + username +
                ", lat=" + lat +
                ", lng=" + lng +
                ", numUpvotes=" + numUpvotes +
                ", numDownvotes=" + numDownvotes +
                ", timeCreated=" + timeCreated +
                ", uuid=" + uuid +
                '}';
    }
}
