package com.mhacks.android.ui.events;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.network.HackathonCallback;
import com.mhacks.android.data.network.NetworkManager;

import org.mhacks.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omkar Moghe on 2/8/2016.
 */
public class DayListFragment extends Fragment {
    public static final String TAG = "DayListFragment";

    // network manager
    private final NetworkManager networkManager = NetworkManager.getInstance();

    // views
    private RecyclerView               mRecyclerView;
    private RecyclerView.Adapter       mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // events
    private ArrayList<Event> events = new ArrayList<>();

    public DayListFragment() {
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.day_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new DayListViewAdapter(events, getContext());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
