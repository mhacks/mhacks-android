//package com.mhacks.app.ui.ticket;
//
//import android.app.Fragment;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import org.mhacks.x.R;
//
///**
// * Created by Omkar Moghe on 10/6/2016.
// */
//
//public class TicketFragment extends Fragment {
//
//    private static final String TAG = "Ti cketFragment";
//    private View mTicketFragView;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater,
//                             ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        mTicketFragView = inflater.inflate(R.layout.fragment_ticket, container, false);
//
//        ImageView qrCode = (ImageView) mTicketFragView.findViewById(R.id.qr_code);
//        TextView nameView = (TextView) mTicketFragView.findViewById(R.id.name);
//
//
////        NetworkManager networkManager = NetworkManager.getInstance();
////        User user = networkManager.getCurrentUser();
////
////        nameView.setText(user.getName());
////        Bitmap qr = QRCode.from(user.getEmail()).withSize(500, 500).bitmap();
////        qrCode.setImageBitmap(qr);
//
//        return mTicketFragView;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//}
