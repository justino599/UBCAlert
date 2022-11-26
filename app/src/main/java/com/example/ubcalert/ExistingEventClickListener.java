package com.example.ubcalert;

public interface ExistingEventClickListener {
    /** Called when the report button of an event is clicked
     * @param holder The EventViewHolder of the event that was clicked
     * @param eventUUID The id of the event in the eventList
     */
    void onReportClick(ExistingEventAdapter.EventViewHolder holder, MyUUID eventUUID);
}
