package com.mhacks.android.ui.schedule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.common.base.Optional;
import com.mhacks.android.R;
import com.mhacks.android.data.model.Event;
import com.mhacks.android.data.model.Venue;
import com.mhacks.android.data.model.Sponsor;
import com.mhacks.android.ui.common.parse.ParseAdapter;
import com.mhacks.android.ui.common.Util;
import com.mhacks.android.ui.common.parse.ViewHolder;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/2/14.
 */
public class EventEditDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
  public static final String TAG = "EventEditDialogFragment";

  private Optional<Event> mEvent = Optional.absent();

  private ParseAdapter<Sponsor> mSponsorAdapter;
  private ParseAdapter<Venue> mLocationAdapter;

  private EditText mTitle;
  private EditText mDetails;
  private Spinner mHost;
  private Spinner mLocation;
  private DatePicker mDate;
  private TimePicker mTime;

  public EventEditDialogFragment() {
    super();
  }

  public static EventEditDialogFragment newInstance(Event event) {
    EventEditDialogFragment result = new EventEditDialogFragment();

    Bundle bundle = new Bundle();
    if (event != null) bundle.putParcelable(Event.CLASS, event);
    result.setArguments(bundle);

    return result;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mEvent = Optional.fromNullable((Event) getArguments().getParcelable(Event.CLASS));

    ParseQueryAdapter.QueryFactory<Sponsor> sponsorFactory = new ParseQueryAdapter.QueryFactory<Sponsor>() {
      @Override
      public ParseQuery<Sponsor> create() {
        return Sponsor.query();
      }
    };
    mSponsorAdapter = new ParseAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mSponsorFiller, sponsorFactory);

    ParseQueryAdapter.QueryFactory<Venue> locationFactory = new ParseQueryAdapter.QueryFactory<Venue>() {
      @Override
      public ParseQuery<Venue> create() {
        return Venue.query();
      }
    };
    mLocationAdapter = new ParseAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mLocationFiller, locationFactory);
  }


  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    LayoutInflater inflater = LayoutInflater.from(getActivity());
    View view = inflater.inflate(R.layout.dialog_event_edit, null);

    mTitle = (EditText) view.findViewById(R.id.event_title);
    mDetails = (EditText) view.findViewById(R.id.event_details);
    mHost = (Spinner) view.findViewById(R.id.event_host);
    mLocation = (Spinner) view.findViewById(R.id.event_location);
    mDate = (DatePicker) view.findViewById(R.id.event_date);
    mTime = (TimePicker) view.findViewById(R.id.event_time);

    mHost.setAdapter(mSponsorAdapter);
    mLocation.setAdapter(mLocationAdapter);

    if (mEvent.isPresent()) {
      mTitle.setText(mEvent.get().getTitle());
      mDetails.setText(mEvent.get().getDetails());
      mHost.setSelection(mSponsorAdapter.indexOf(mEvent.get().getHost()));
      mLocation.setSelection(mLocationAdapter.indexOf(mEvent.get().getLocation()));

      Calendar calendar = GregorianCalendar.getInstance();
      calendar.setTime(mEvent.get().getTime());

      mDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
      mTime.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
      mTime.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

    return new AlertDialog.Builder(getActivity())
      .setTitle(R.string.events_new)
      .setView(view)
      .setPositiveButton(android.R.string.ok, this)
      .setNegativeButton(android.R.string.cancel, this)
      .create();
  }

  @Override
  public void onClick(DialogInterface dialogInterface, int i) {
    switch (i) {
      case DialogInterface.BUTTON_POSITIVE:
        Event event = mEvent.isPresent() ? mEvent.get() : new Event();
        event
          .setTitle(mTitle.getText().toString())
          .setDetails(mDetails.getText().toString())
          .setHost(mSponsorAdapter.getItem(mHost.getSelectedItemPosition()))
          .setLocation(mLocationAdapter.getItem(mLocation.getSelectedItemPosition()))
          .setTime(Util.Time.fromPickers(mDate, mTime).getTime())
          .saveEventually();

        dismiss();
        break;
      case DialogInterface.BUTTON_NEGATIVE:
        getDialog().cancel();
        break;
    }
  }

  private ParseAdapter.ListCallbacks<Sponsor> mSponsorFiller = new ParseAdapter.ListCallbacks<Sponsor>() {
    @Override
    public void populateView(ViewHolder holder, Sponsor sponsor, boolean hasSectionHeader, boolean hasSectionFooter) {
      TextView text = holder.get(android.R.id.text1);
      text.setText(sponsor.getTitle());
    }
  };

  private ParseAdapter.ListCallbacks<Venue> mLocationFiller = new ParseAdapter.ListCallbacks<Venue>() {
    @Override
    public void populateView(ViewHolder holder, Venue venue, boolean hasSectionHeader, boolean hasSectionFooter) {
      TextView text = holder.get(android.R.id.text1);
      text.setText(venue.getTitle());
    }
  };
}
