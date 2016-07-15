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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.mhacks.android.R;

/**
 * Created by Riyu on 7/7/16.
 */
public class RegistrationFragment extends Fragment{
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    Button button;
    TextView text_content;
    TextView text_format;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration , container, false);
        button =  (Button)  view.findViewById(R.id.qr_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scanQR(v);
            }
        });
        text_format = (TextView) view.findViewById(R.id.scan_info_format);
        text_content = (TextView) view.findViewById(R.id.scan_info_content);
        return view;
    }

    public void scanQR(View v){
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        }
        catch (ActivityNotFoundException anfe){
            showDialog(RegistrationFragment.this.getActivity(), "No Scanner Found", "Download Scanner?", "yes", "no").show();
        }
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
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                text_format.setText(format);
                text_content.setText(contents);
            }
        }
    }

}
