package com.mhacks.android.ui.announcements;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.mhacks.android.R;
import com.mhacks.android.data.model.Announcement;
import com.mhacks.android.data.model.Venue;
import com.mhacks.android.data.model.Sponsor;
import com.mhacks.android.ui.common.parse.ParseAdapter;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/2/14.
 */
public class AnnouncementEditDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, ParseAdapter.ListCallbacks<Sponsor> {
  public static final String TAG = "AnnouncementEditDialogFragment";

  private Optional<Announcement> mAnnouncement = Optional.absent();

  private ParseAdapter<Sponsor> mSponsorAdapter;
  private ParseAdapter<Venue> mLocationAdapter;

  private EditText mTitle;
  private EditText mDetails;
  private Spinner mPoster;
  private CheckBox mPinned;
  private CheckBox mPush;

  public AnnouncementEditDialogFragment() {
    super();
  }

  public static AnnouncementEditDialogFragment newInstance(Announcement announcement) {
    AnnouncementEditDialogFragment result = new AnnouncementEditDialogFragment();

    Bundle bundle = new Bundle();
    if (announcement != null) bundle.putParcelable(Announcement.CLASS, announcement);
    result.setArguments(bundle);

    return result;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mAnnouncement = Optional.fromNullable((Announcement) getArguments().getParcelable(Announcement.CLASS));

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
    View view = inflater.inflate(R.layout.dialog_announcement_edit, null);

    mTitle = (EditText) view.findViewById(R.id.announcement_title);
    mDetails = (EditText) view.findViewById(R.id.announcement_details);
    mPoster = (Spinner) view.findViewById(R.id.announcement_poster);
    mPinned = (CheckBox) view.findViewById(R.id.announcement_pinned);
    mPush = (CheckBox) view.findViewById(R.id.announcement_push);

    mPoster.setAdapter(mSponsorAdapter);

    if (mAnnouncement.isPresent()) {
      mTitle.setText(mAnnouncement.get().getTitle());
      mDetails.setText(mAnnouncement.get().getDetails());
      mPoster.setSelection(mSponsorAdapter.indexOf(mAnnouncement.get().getPoster()));
      mPush.setChecked(false);
      mPinned.setChecked(mAnnouncement.get().isPinned());
    }

    return new AlertDialog.Builder(getActivity())
      .setTitle(R.string.announcements_new)
      .setView(view)
      .setPositiveButton(android.R.string.ok, this)
      .setNegativeButton(android.R.string.cancel, this)
      .create();
  }

  @Override
  public void onClick(DialogInterface dialogInterface, int i) {
    switch (i) {
      case DialogInterface.BUTTON_POSITIVE:
        Announcement announcement = mAnnouncement.isPresent() ? mAnnouncement.get() : new Announcement();
        announcement
          .setTitle(mTitle.getText().toString())
          .setDetails(mDetails.getText().toString())
          .setPoster(mSponsorAdapter.getItem(mPoster.getSelectedItemPosition()))
          .setPinned(mPinned.isChecked())
          .saveEventually();

        if (mPush.isChecked()) announcement.push();

        dismiss();
        break;
      case DialogInterface.BUTTON_NEGATIVE:
        getDialog().cancel();
        break;
    }
  }

  @Override
  public void populateView(ParseAdapter.ViewHolder holder, Sponsor sponsor, boolean hasSectionHeader, boolean hasSectionFooter) {
    TextView text = holder.get(android.R.id.text1);
    text.setText(sponsor.getTitle());
  }
}
