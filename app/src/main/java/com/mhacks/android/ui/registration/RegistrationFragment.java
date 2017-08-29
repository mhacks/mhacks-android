package com.mhacks.android.ui.registration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mhacks.android.data.model.Scan;
import com.mhacks.android.data.model.ScanData;
import com.mhacks.android.data.model.ScanEvent;
import com.mhacks.android.data.network.HackathonCallback;
import com.mhacks.android.data.network.NetworkManager;

import org.mhacks.android.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Riyu on 7/7/16.
 * Updated by omkarmoghe on 10/6/16
 */
public class RegistrationFragment extends Fragment{
    public static final String TAG = "RegistrationFragment";

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private View mView;
    private FloatingActionButton registerButton;
    private TextView             textContent;
    private LinearLayout         scanDetails;
    private Spinner              scanType;
    private ProgressBar          loadingData;

    private HashMap<String, ScanEvent> scanEvents;

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence button_yes, CharSequence button_no) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(button_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.google.zxing.client.android")));
                } catch (ActivityNotFoundException anfe) {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_registration , container, false);

        final NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.getScanEvents(new HackathonCallback<List<ScanEvent>>() {
            @Override
            public void success(List<ScanEvent> response) {
                scanEvents = new HashMap<>();
                for (ScanEvent scanEvent : response) {
                    scanEvents.put(scanEvent.getName(), scanEvent);
                }
                buildScanTypes();
            }

            @Override
            public void failure(Throwable error) {
                Log.e(TAG, "shit", error);
            }
        });

        scanType = (Spinner) mView.findViewById(R.id.scan_type);
        scanDetails = (LinearLayout) mView.findViewById(R.id.scan_details);
        registerButton =  (FloatingActionButton)  mView.findViewById(R.id.scan);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scanQR(v, "REG");
            }
        });
        textContent = (EditText) mView.findViewById(R.id.scan_info_content);
        textContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    loadingData.setVisibility(View.GONE);
                    scanDetails.removeViews(1, scanDetails.getChildCount() - 1);
                } else performScan();
            }
        });
        loadingData = (ProgressBar) mView.findViewById(R.id.loading_data);

        return mView;
    }

    public void scanQR(View v, String qr_type){
        scanDetails.removeViews(1, scanDetails.getChildCount() - 1);

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

    public void performScan() {
        if (!NetworkManager.getInstance().getCurrentUser().isCanPerformScan()) {
            Snackbar.make(mView, "You do not have permissions to scan :(", Snackbar.LENGTH_SHORT).show();
            return;
        }

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                         ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 10;

        String userId = textContent.getText().toString();
        loadingData.setVisibility(View.VISIBLE);

        String scanName = scanType.getSelectedItem().toString();
        String scanId = scanEvents.get(scanName).getId();

        final NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.performScan(userId, scanId, new HackathonCallback<Scan>() {
            @Override
            public void success(Scan response) {
                loadingData.setVisibility(View.GONE);
                scanDetails.removeViews(1, scanDetails.getChildCount() - 1);

                for (ScanData data : response.getData()) {
                    TextView textView = new TextView(getActivity());
                    textView.setText(data.getLabel() + ": " + data.getValue());
                    textView.setTextColor(data.getColorHex());
                    textView.setTextSize(14);
                    scanDetails.addView(textView, params);
                }

                if (response.isScanned()) {
                    Button button = new Button(getActivity());
                    button.setText("Confirm Scan");
                    button.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            confirmScan();
                        }
                    });

                    scanDetails.addView(button, params);
                }
            }

            @Override
            public void failure(Throwable error) {
                Snackbar.make(mView, "Unable to perform scan", Snackbar.LENGTH_SHORT).show();
                loadingData.setVisibility(View.GONE);
            }
        });
    }

    public void confirmScan() {
        if (!NetworkManager.getInstance().getCurrentUser().isCanPerformScan()) {
            Snackbar.make(mView, "You do not have permissions to scan :(", Snackbar.LENGTH_SHORT).show();
            return;
        }

        String userId = textContent.getText().toString();
        loadingData.setVisibility(View.VISIBLE);

        String scanName = scanType.getSelectedItem().toString();
        String scanId = scanEvents.get(scanName).getId();

        final NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.confirmScan(userId, scanId, new HackathonCallback<Scan>() {
            @Override
            public void success(Scan response) {
                Snackbar.make(mView, "Scan confirmed!", Snackbar.LENGTH_SHORT).show();
                loadingData.setVisibility(View.GONE);
            }

            @Override
            public void failure(Throwable error) {
                Snackbar.make(mView, "Unable to confirm scan", Snackbar.LENGTH_SHORT).show();
                loadingData.setVisibility(View.GONE);
            }
        });
    }

    public void buildScanTypes() {
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (String scanEventName : scanEvents.keySet()) {
            spinnerAdapter.add(scanEventName);
        }

        scanType.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == 0){
            if (resultCode == Activity.RESULT_OK){
                String contents = intent.getStringExtra("SCAN_RESULT");

                textContent.setText(contents);

                performScan();
            }
        }
    }
}
