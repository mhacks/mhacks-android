package com.mhacks.android.ui.messages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.firebase.client.Firebase;
import com.mhacks.android.data.firebase.MessageThread;
import com.mhacks.android.data.firebase.ThreadMessage;
import com.mhacks.android.data.model.User;
import com.mhacks.iv.android.R;
import com.viewpagerindicator.TitlePageIndicator;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/24/14.
 */
public class ThreadsFragment extends Fragment implements
  View.OnClickListener, ThreadMessagesFragmentAdapter.OnThreadsUpdatedListener {
  public static final String TAG = "ThreadsFragment";

  public static final String PARTNER = "partner";
  public static final String PRIVATE = "private";
  public static final String THREADS = "threads";
  public static final String MESSAGES = "messages";

  private User mPendingPartner;
  private String mFirebaseUrl;
  private Firebase mMessages;
  private Firebase mPrivate;
  private Firebase mThreads;

  private TitlePageIndicator mIndicator;
  private ImageButton mSendButton;
  private RelativeLayout mHandle;
  private RelativeLayout mLayout;
  private LinearLayout mLoading;
  private ListView mListView;
  private ViewPager mPager;
  private EditText mInput;

  private Animation mSlideInAnimation;
  private Animation mSlideOutAnimation;

  private ThreadMessagesFragmentAdapter mAdapter;

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

    Bundle args = getArguments();
    if (savedInstanceState == null && args != null && args.containsKey(PARTNER)) {
      mPendingPartner = args.getParcelable(PARTNER);
      MessageThread.push(mPendingPartner, mPrivate);
    }

    mSlideInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_up);
    mSlideOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_down);

    mAdapter = new ThreadMessagesFragmentAdapter(getActivity(), getChildFragmentManager(), mThreads, mMessages).setListener(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_threads, container, false);
    mSendButton = (ImageButton) mLayout.findViewById(R.id.chat_send_button);
    mLoading = (LinearLayout) mLayout.findViewById(R.id.threads_loading);
    mHandle = (RelativeLayout) mLayout.findViewById(R.id.chat_handle);
    mListView = (ListView) mLayout.findViewById(R.id.chat_list);
    mInput = (EditText) mLayout.findViewById(R.id.chat_input);

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
    setThreadsPresent(count > 0);

    if (mPendingPartner != null) {
      int index = mAdapter.indexByUserId(mPendingPartner.getObjectId());
      if (index != ThreadMessagesFragmentAdapter.NONE) {
        mPager.setCurrentItem(index);
        mPendingPartner = null;
      }
    }
  }

  // this is disgusting
  private void setThreadsPresent(boolean present) {
    if (present) {
      if (mIndicator.getVisibility() == View.GONE) {
        mIndicator.startAnimation(mSlideInAnimation);
      }
      mIndicator.setVisibility(View.VISIBLE);
      if (mHandle.getVisibility() == View.GONE) {
        mHandle.startAnimation(mSlideInAnimation);
      }
      mHandle.setVisibility(View.VISIBLE);
      mLoading.setVisibility(View.GONE);
      mPager.setVisibility(View.VISIBLE);
    } else {
      if (mIndicator.getVisibility() == View.VISIBLE) {
        mIndicator.startAnimation(mSlideOutAnimation);
      }
      mIndicator.setVisibility(View.GONE);
      if (mHandle.getVisibility() == View.VISIBLE) {
        mHandle.startAnimation(mSlideOutAnimation);
      }
      mHandle.setVisibility(View.GONE);
      mLoading.setVisibility(View.VISIBLE);
      mPager.setVisibility(View.GONE);
    }
  }
}
