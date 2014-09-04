package com.mhacks.android.ui.messages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.firebase.client.Firebase;
import com.mhacks.android.R;
import com.mhacks.android.data.firebase.ThreadMessage;
import com.mhacks.android.data.model.User;
import com.viewpagerindicator.TitlePageIndicator;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/24/14.
 */
public class ThreadsFragment extends Fragment implements
  View.OnClickListener, ThreadMessagesFragmentAdapter.OnThreadsUpdatedListener {
  public static final String TAG = "ThreadsFragment";

  public static final String PRIVATE = "private";
  public static final String THREADS = "threads";
  public static final String MESSAGES = "messages";

  private String mFirebaseUrl;
  private Firebase mPrivate;
  private Firebase mMessages;
  private Firebase mThreads;
  private ThreadMessagesFragmentAdapter mAdapter;

  private RelativeLayout mLayout;
  private ListView mListView;
  private EditText mInput;
  private ImageButton mSendButton;
  private TitlePageIndicator mIndicator;
  private ViewPager mPager;

  public ThreadsFragment() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    mFirebaseUrl = getString(R.string.firebase_url);
    mPrivate = new Firebase(mFirebaseUrl).child(PRIVATE);

    mThreads = mPrivate.child(THREADS).child(User.getCurrentUser().getObjectId());
    mMessages = mPrivate.child(MESSAGES);

    mAdapter = new ThreadMessagesFragmentAdapter(getChildFragmentManager(), mThreads, mMessages).setListener(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_threads, container, false);
    mListView = (ListView) mLayout.findViewById(R.id.chat_list);
    mInput = (EditText) mLayout.findViewById(R.id.chat_input);
    mSendButton = (ImageButton) mLayout.findViewById(R.id.chat_send_button);

    mIndicator = (TitlePageIndicator) mLayout.findViewById(R.id.threads_pager_indicator);
    mPager = (ViewPager) mLayout.findViewById(R.id.threads_pager);

    return mLayout;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mSendButton.setOnClickListener(this);
    mPager.setAdapter(mAdapter);
    mIndicator.setViewPager(mPager);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mAdapter.cleanup();
  }

  @Override
  public void onClick(View view) {
    CharSequence text = mInput.getText();
    if (text.length() > 0) {
      ThreadMessage.push(text.toString(), mAdapter.getThread(mPager.getCurrentItem()), mPrivate);
      mInput.setText(null);
    }
  }

  @Override
  public void onThreadsUpdated(int count) {
    mIndicator.notifyDataSetChanged();
  }
}
