package com.example.ubcalert;

import android.view.View;

public interface EventClickListener {
    /** Called when the share button of an event is clicked
     * @param holder The EventViewHolder of the event that was clicked
     * @param position The index of the event in the eventList
     */
    void onShareClick(EventAdapter.EventViewHolder holder, int position);

    /** Called when the report button of an event is clicked
     * @param holder The EventViewHolder of the event that was clicked
     * @param position The index of the event in the eventList
     */
    void onReportClick(EventAdapter.EventViewHolder holder, int position);
}
