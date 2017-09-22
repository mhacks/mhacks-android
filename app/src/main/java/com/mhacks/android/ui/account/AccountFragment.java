package com.mhacks.android.ui.account;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mhacks.android.ui.settings.SettingsFragment;

import org.mhacks.x.R;

/**
 * Created by Omkar Moghe on 10/7/2016.
 */

public class AccountFragment extends Fragment {
    private View mView;
    EditText usernameView;
    EditText passwordView;
    TextView loginStatusView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_account, container, false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String username = sharedPref.getString(SettingsFragment.USERNAME_KEY, "");
        final String password = sharedPref.getString(SettingsFragment.PASSWORD_KEY, "");


        usernameView = (EditText) mView.findViewById(R.id.username);
        passwordView = (EditText) mView.findViewById(R.id.password);
        loginStatusView = (TextView) mView.findViewById(R.id.login_status);

        if (!username.isEmpty()) usernameView.setText(username);
        if (!password.isEmpty()) passwordView.setText(password);

//        if (NetworkManager.getInstance().getCurrentUser() == null) loginStatusView.setText(R.string.logged_out);
        else loginStatusView.setText(R.string.logged_in);

        Button login = (Button) mView.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usernameView.getText().toString().isEmpty()) {
                    usernameView.setError("Please enter your email.");
                    return;
                }
                if (passwordView.getText().toString().isEmpty()) {
                    passwordView.setError("Please enter your password.");
                    return;
                }

                login(usernameView.getText().toString(), passwordView.getText().toString());
            }
        });

        Button logout = (Button) mView.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NetworkManager.getInstance().logout();
                usernameView.setText("");
                passwordView.setText("");

                updatePrefs();
            }
        });

        return mView;
    }

    public void updatePrefs() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPref.edit()
                  .putString(SettingsFragment.USERNAME_KEY, usernameView.getText().toString())
                  .putString(SettingsFragment.PASSWORD_KEY, passwordView.getText().toString())
                  .apply();
    }

    public void login(final String username, final String password) {
//        final NetworkManager networkManager = NetworkManager.getInstance();
//        networkManager.attemptLogin(username, password, new HackathonCallback<User>() {
//            @Override
//            public void success(User response) {
//                Snackbar.make(mView, "Login successful!", Snackbar.LENGTH_SHORT).show();
//                loginStatusView.setText(R.string.logged_in);
//
//                // save in preferences
//                updatePrefs();
//            }
//
//            @Override
//            public void failure(Throwable error) {
//                usernameView.setError("Incorrect username or password");
//                passwordView.setError("Incorrect username or password");
//            }
//        });
    }
}
