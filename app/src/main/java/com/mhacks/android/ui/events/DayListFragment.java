package com.mhacks.android.ui.events;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mhacks.android.R;

/**
 * Created by Omkar Moghe on 2/8/2016.
 */
public class DayListFragment extends Fragment {

    private RecyclerView               mRecyclerView;
    private RecyclerView.Adapter       mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public DayListFragment() {
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


        return view;
    }
}
