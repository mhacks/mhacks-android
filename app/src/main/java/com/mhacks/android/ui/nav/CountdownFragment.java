package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;

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
    private ProgressBar mCircularProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countdown, container, false);

        mCircularProgress = (ProgressBar) view.findViewById(R.id.progressbar_counter);

        // Animation testing
    //    Animation animation = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
    //    animation.setFillAfter(true);
    //    mCircularProgress.startAnimation(animation);

        return view;
    }
}
