package com.mhacks.android.ui.registration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mhacks.android.data.model.ScanEvent;
import com.mhacks.android.data.network.HackathonCallback;
import com.mhacks.android.data.network.NetworkManager;

import org.mhacks.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Riyu on 7/7/16.
 * Updated by omkarmoghe on 10/6/16
 */
public class RegistrationFragment extends Fragment{
    public static final String TAG = "RegistrationFragment";

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private FloatingActionButton registerButton;
    private TextView             textContent;
    private EditText             textFormat;
    private LinearLayout         scanActions;
    private Spinner              scanType;

    private ArrayList<ScanEvent> scanEventList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration , container, false);

        final NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.getScanEvents(new HackathonCallback<List<ScanEvent>>() {
            @Override
            public void success(List<ScanEvent> response) {
                Log.d(TAG, "it worked?");
                scanEventList = new ArrayList<>(response);
                buildScanTypes();
            }

            @Override
            public void failure(Throwable error) {
                Log.e(TAG, "shit", error);
            }
        });

        scanType = (Spinner) view.findViewById(R.id.scan_type);
        scanActions = (LinearLayout) view.findViewById(R.id.scan_actions);
        registerButton =  (FloatingActionButton)  view.findViewById(R.id.scan);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scanQR(v, "REG");
            }
        });
        textContent = (EditText) view.findViewById(R.id.scan_info_content);
        textContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                scanActions.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    public void scanQR(View v, String qr_type){
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("QR_Type", qr_type);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        }
        catch (ActivityNotFoundException anfe){
            showDialog(RegistrationFragment.this.getActivity(), "No Scanner Found", "Download Scanner?", "yes", "no").show();
        }
    }

    public void peformScan(String scanId) {

    }

    public void buildScanTypes() {
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (final ScanEvent scanEvent : scanEventList) {
            spinnerAdapter.add(scanEvent.getName());
        }

        scanType.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence button_yes, CharSequence button_no) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(button_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.google.zxing.client.android")));
                } catch (ActivityNotFoundException anfe){

                }
            }
        });
        downloadDialog.setNegativeButton(button_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == 0){
            if (resultCode == Activity.RESULT_OK){
                String contents = intent.getStringExtra("SCAN_RESULT");

                textContent.setText(contents);
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
    }
}
