package com.mhacks.android.ui.concierge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.mhacks.android.R;
import com.mhacks.android.data.model.Sponsor;
import com.mhacks.android.data.model.User;
import com.mhacks.android.ui.common.ParseAdapter;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/2/14.
 */
public class UserEditDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, ParseAdapter.ListCallbacks<Sponsor> {
  public static final String TAG = "ContactEditDialogFragment";

  private Optional<User> mUser = Optional.absent();

  private ParseAdapter<Sponsor> mSponsorAdapter;

  private EditText mName;
  private EditText mPosition;
  private Spinner mSponsor;

  public UserEditDialogFragment() {
    super();
  }

  public static UserEditDialogFragment newInstance(User user) {
    UserEditDialogFragment result = new UserEditDialogFragment();

    Bundle bundle = new Bundle();
    if (user != null) bundle.putParcelable(User.CLASS, user);
    result.setArguments(bundle);

    return result;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mUser = Optional.fromNullable(getArguments().<User>getParcelable(User.CLASS));

    ParseQueryAdapter.QueryFactory<Sponsor> sponsorFactory = new ParseQueryAdapter.QueryFactory<Sponsor>() {
      @Override
      public ParseQuery<Sponsor> create() {
        return Sponsor.query();
      }
    };
    mSponsorAdapter = new ParseAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, this, sponsorFactory);
  }


  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    LayoutInflater inflater = LayoutInflater.from(getActivity());
    View view = inflater.inflate(R.layout.dialog_user_edit, null);

    mName = (EditText) view.findViewById(R.id.user_name);
    mPosition = (EditText) view.findViewById(R.id.user_position);
    mSponsor = (Spinner) view.findViewById(R.id.user_sponsor);

    mSponsor.setAdapter(mSponsorAdapter);

    if (mUser.isPresent()) {
      mName.setText(mUser.get().getName());
      mPosition.setText(mUser.get().getPosition());
      mSponsor.setSelection(mSponsorAdapter.indexOf(mUser.get().getSponsor()));
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
      .setTitle(R.string.user_new)
      .setView(view)
      .setPositiveButton(android.R.string.ok, this)
      .setNeutralButton(android.R.string.cancel, this);
    if (mUser.isPresent()) builder.setNegativeButton(R.string.delete, this);

    return builder.create();
  }

  @Override
  public void onClick(DialogInterface dialogInterface, int i) {
    switch (i) {
      case DialogInterface.BUTTON_POSITIVE:
        User user = mUser.isPresent() ? mUser.get() : new User();
        user
          .setName(mName.getText().toString())
          .setPosition(mPosition.getText().toString())
          .setSponsor(mSponsorAdapter.getItem(mSponsor.getSelectedItemPosition()))
          .saveLater();

        dismiss();
        break;
      case DialogInterface.BUTTON_NEUTRAL:
        getDialog().cancel();
        break;
      case DialogInterface.BUTTON_NEGATIVE:
        new AlertDialog.Builder(getActivity())
          .setTitle(R.string.confirm_delete)
          .setMessage(R.string.confirm_delete_message)
          .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              mUser.get().deleteEventually();
              dialogInterface.dismiss();
            }
          })
          .setNegativeButton(android.R.string.cancel, null)
          .show();
        break;
    }
  }

  @Override
  public void populateView(ParseAdapter.ViewHolder holder, Sponsor sponsor, boolean hasSectionHeader, boolean hasSectionFooter) {
    TextView text = holder.get(android.R.id.text1);
    text.setText(sponsor.getTitle());
  }
}
