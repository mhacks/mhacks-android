package com.mhacks.android.ui.events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mhacks.android.data.model.Event;

import org.mhacks.android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Omkar Moghe on 2/8/2016.
 */
public class DayListViewAdapter extends RecyclerView.Adapter<DayListViewAdapter.DayViewHolder> {

    private ArrayList<Event> events;
    private Context context;

    public static class DayViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout mEventColor;
        public TextView mEventTitle;
        public TextView mEventTime;
        public TextView mEventInfo;

        public DayViewHolder(View v) {
            super(v);

            mEventColor = (FrameLayout) v.findViewById(R.id.event_color);
            mEventTitle = (TextView) v.findViewById(R.id.event_title);
            mEventTime = (TextView) v.findViewById(R.id.event_time);
            mEventInfo = (TextView) v.findViewById(R.id.event_info);
        }
    }

    public DayListViewAdapter(ArrayList<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);

        return new DayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        Event e = events.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);

        holder.mEventTitle.setText(e.getName());
        holder.mEventTime.setText(sdf.format(e.getStartTime()));
        holder.mEventInfo.setText(e.getInfo());

        switch (e.getCategory()) {
            case 0: // Logistics - Blue
                holder.mEventColor.setBackgroundColor(context.getResources().getColor(R.color.event_blue));
                break;
            case 1: // Social - Red
                holder.mEventColor.setBackgroundColor(context.getResources().getColor(R.color.event_red));
                break;
            case 2: // Food - MAIZE
                holder.mEventColor.setBackgroundColor(context.getResources().getColor(R.color.event_yellow));
                break;
            case 3: // Tech Talk - Purple
                holder.mEventColor.setBackgroundColor(context.getResources().getColor(R.color.event_purple));
                break;
            case 4: // Other - Green
                holder.mEventColor.setBackgroundColor(context.getResources().getColor(R.color.event_green));
                break;
            default: // go blue
                holder.mEventColor.setBackgroundColor(context.getResources().getColor(R.color.event_blue));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
