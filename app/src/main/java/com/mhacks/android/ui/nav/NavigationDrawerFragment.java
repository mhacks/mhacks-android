package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mhacks.iv.android.R;

/**
 * Created by Omkar Moghe on 10/22/2014.
 */
public class NavigationDrawerFragment extends Fragment implements View.OnClickListener {
    private View mNavDrawerView;

    private TextView announcementsTextView, scheduleTextView, sponsorsTextView, awardsTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mNavDrawerView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        //TODO instantiate all text view
        //TODO set onclicklisteners for all textviews

        return mNavDrawerView;
    }

    @Override
    public void onClick(View view) {
        //TODO text click logic to change views, action bar, etc.
    }

    public interface NavigationDrawerCallbacks {

    }
}
