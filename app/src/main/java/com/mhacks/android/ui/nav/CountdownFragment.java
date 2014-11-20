package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mhacks.iv.android.R;

/**
 * Created by jawad on 04/11/14.
 */
/* TODO List
 * 1) Get data from parse
 * 2) Create countdown object+layout+settings
 * 3) Apply data/time to countdown
 */
public class CountdownFragment extends Fragment {
    private View mCountdownFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCountdownFragment = inflater.inflate(R.layout.fragment_countdown, container, false);

        //Put code here <3

        return mCountdownFragment;
    }
}
