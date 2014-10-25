package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mhacks.iv.android.R;

/**
 * Created by Omkar Moghe on 10/25/2014.
 */
public class SponsorsFragment extends Fragment{

    private View mSponsorsFragView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mSponsorsFragView = inflater.inflate(R.layout.fragment_sponsors, container, false);

        //Put code for instantiating views, etc here. (before the return statement.)

        return mSponsorsFragView;
    }
}
