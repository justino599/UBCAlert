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

public class ExistingEventAdapter extends RecyclerView.Adapter<ExistingEventAdapter.EventViewHolder> {
    private final Context context;
    private final ArrayList<Event> eventList;
    private ExistingEventClickListener eventClickListener;

    public ExistingEventAdapter(Context context, ArrayList<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.existing_event_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.title.setText(event.getTitle());
        holder.location.setText(String.format("Location: %s",event.getLocation()));
        holder.time.setText(String.format("Time: %s", DateTimeFormatter.ofPattern("hh:mm a EEE MMM dd, yyyy").format(event.timeCreatedGetter())));
        holder.uuid = eventList.get(position).getUuid();

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void setEventClickListener(ExistingEventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        final TextView title, location, time;
        final androidx.appcompat.widget.AppCompatImageView shareButton, addReportButton;
        MyUUID uuid = null;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.eventTitle);
            location = itemView.findViewById(R.id.eventLocation);
            time = itemView.findViewById(R.id.eventTime);
            shareButton = itemView.findViewById(R.id.shareButton);
            addReportButton = itemView.findViewById(R.id.addReportButton);

            addReportButton.setOnClickListener(v -> eventClickListener.onReportClick(this, uuid));
        }
    }
}
