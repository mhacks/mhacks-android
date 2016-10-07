package com.mhacks.android.ui.map;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mhacks.android.data.model.Floor;
import com.mhacks.android.data.network.HackathonCallback;
import com.mhacks.android.data.network.NetworkManager;

import org.mhacks.android.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anksh on 12/31/2014.
 * Updated by omkarmoghe on 10/6/16
 *
 * Displays maps of the MHacks 8 venues.
 */
public class MapViewFragment extends Fragment {

    public static final String TAG = "MapViewFragment";

    // Views
    private    View      mMapFragView;
    public     Spinner  nameView;
    public TextView  descriptionView;
    public ImageView imageView;

    // Data
    ArrayList<Floor> floors;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mMapFragView = inflater.inflate(R.layout.fragment_map, container, false);

        nameView = (Spinner) mMapFragView.findViewById(R.id.name);
        descriptionView =(TextView) mMapFragView.findViewById(R.id.description);
        imageView = (ImageView) mMapFragView.findViewById(R.id.image);

        final NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.getFloors(new HackathonCallback<List<Floor>>() {
            @Override
            public void success(List<Floor> response) {
                floors = new ArrayList<>(response);

                ArrayAdapter<String> spinnerAdapter =
                        new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                if (!floors.isEmpty()) {
                    for (Floor floor : floors) {

                        spinnerAdapter.add(floor.getName());

                    }
                    nameView.setAdapter(spinnerAdapter);
                    spinnerAdapter.notifyDataSetChanged();
                    nameView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView,
                                                   View view,
                                                   int i,
                                                   long l) {
                            Floor f = floors.get(i);

                            descriptionView.setText(f.getDescription());

                            networkManager.getImage(f.getImage(), new HackathonCallback<Bitmap>() {
                                @Override
                                public void success(Bitmap response) {
                                    final Bitmap image = response;
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            imageView.setImageBitmap(image);
                                        }
                                    });
                                }

                                @Override
                                public void failure(Throwable error) {

                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

//                    Floor f = floors.get(0);
//
//                    descriptionView.setText(f.getDescription());
//
//                    Log.d(TAG, f.getImage());

                }
            }

            @Override
            public void failure(Throwable error) {

            }
        });


        return mMapFragView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
