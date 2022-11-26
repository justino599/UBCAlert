package com.example.ubcalert;

public interface EventClickListener {
    /** Called when the share button of an event is clicked
     * @param holder The EventViewHolder of the event that was clicked
     * @param eventUUID The id of the event in the eventList
     */
    void onShareClick(EventAdapter.EventViewHolder holder, MyUUID eventUUID);

    /** Called when the report button of an event is clicked
     * @param holder The EventViewHolder of the event that was clicked
     * @param eventUUID The id of the event in the eventList
     */
    void onReportClick(EventAdapter.EventViewHolder holder, MyUUID eventUUID);
}
