package com.example.ubcalert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private final Context context;
    private final ArrayList<Event> eventList;
    private EventClickListener eventClickListener;

    public EventAdapter(Context context, ArrayList<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.event_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.title.setText(event.getTitle());
        holder.location.setText(String.format("Location: %s",event.getLocation()));
        holder.time.setText(String.format("Time: %s", DateTimeFormatter.ofPattern("hh:mm a EEE MMM dd, yyyy").format(event.timeCreatedGetter())));

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void setEventClickListener(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        final TextView title, location, time;
        final androidx.appcompat.widget.AppCompatImageView shareButton, addReportButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.eventTitle);
            location = itemView.findViewById(R.id.eventLocation);
            time = itemView.findViewById(R.id.eventTime);
            shareButton = itemView.findViewById(R.id.shareButton);
            addReportButton = itemView.findViewById(R.id.addReportButton);

            shareButton.setOnClickListener(v -> eventClickListener.onShareClick(this, getAbsoluteAdapterPosition()));
            addReportButton.setOnClickListener(v -> eventClickListener.onReportClick(this, getAbsoluteAdapterPosition()));
        }
    }
}
