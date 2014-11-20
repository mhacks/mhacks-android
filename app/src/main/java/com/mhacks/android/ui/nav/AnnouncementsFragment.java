package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mhacks.iv.android.R;

/**
 * Created by Omkar Moghe on 10/25/2014.
 */
public class AnnouncementsFragment extends Fragment{
    private View mAnnouncementsFragView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAnnouncementsFragView = inflater.inflate(R.layout.fragment_announcements, container, false);

        //Put code for instantiating views, etc here. (before the return statement.)

        // Add in the countdown timer
        initCountdownTimer();

        return mAnnouncementsFragView;
    }

    private void initCountdownTimer() {
        // Create and setup the countdown timer frag, as necessary
        CountdownFragment frag = new CountdownFragment();

        // Add the fragment to this fragment's designated "container"
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.container_base, frag);
        fragmentTransaction.commit();
    }
}
