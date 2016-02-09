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
    private ArrayList<Event> events;

    public DayListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkManager.getEvents(new HackathonCallback<List<Event>>() {
            @Override
            public void success(List<Event> response) {
                events = new ArrayList<Event>(response);
                updateRecyclerView();
            }

            @Override
            public void failure(Throwable error) {
                Log.e(TAG, "ruh roh", error);
            }
        });
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

        return view;
    }

    private void updateRecyclerView() {
        mAdapter = new DayListViewAdapter(events, getContext());
        mRecyclerView.setAdapter(mAdapter);
    }
}
