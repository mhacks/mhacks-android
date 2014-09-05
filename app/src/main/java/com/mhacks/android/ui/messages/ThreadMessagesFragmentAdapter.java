package com.mhacks.android.ui.messages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bugsnag.android.Bugsnag;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.mhacks.android.data.firebase.MessageThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 9/3/14.
 */

public class ThreadMessagesFragmentAdapter extends FragmentStatePagerAdapter implements
  ChildEventListener {

  public static final String ACTION_THREAD_CLOSED = "com.mhacks.android.ACTION_THREAD_CLOSED";
  public static final String THREAD_ID = "threadId";

  public static final int NONE = -1;

  private final Firebase mUserThreads;
  private final Firebase mMessages;
  private final Context mContext;
  private final List<MessageThread> mThreads = new ArrayList<>();
  private final Map<String, MessageThread> mThreadsMap = Maps.newHashMap();
  private final Map<String, MessageThread> mThreadsByUser = Maps.newHashMap();

  private Optional<OnThreadsUpdatedListener> mListener = Optional.absent();

  private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent == null || !intent.hasExtra(THREAD_ID)) return;
      onThreadClosed(intent.getStringExtra(THREAD_ID));
    }
  };

  public ThreadMessagesFragmentAdapter(Context context, FragmentManager fm, Firebase userThreads, Firebase messages) {
    super(fm);
    mContext = context;
    mUserThreads = userThreads;
    mMessages = messages;
    mUserThreads.addChildEventListener(this);
    mContext.registerReceiver(mBroadcastReceiver, new IntentFilter(ACTION_THREAD_CLOSED));
  }

  public void cleanup() {
    mUserThreads.removeEventListener(this);
    mThreads.clear();
    mThreadsMap.clear();
    mContext.unregisterReceiver(mBroadcastReceiver);
  }

  @Override
  public Fragment getItem(int position) {
    return ThreadMessagesFragment.newInstance(mMessages.child(mThreads.get(position).getThreadId()));
  }

  @Override
  public int getCount() {
    return mThreads.size();
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return mThreads.get(position).getPartnerName();
  }

  @Override
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

  @Override
  public void notifyDataSetChanged() {
    super.notifyDataSetChanged();
    if (mListener.isPresent()) mListener.get().onThreadsUpdated(getCount());
  }

  public ThreadMessagesFragmentAdapter setListener(OnThreadsUpdatedListener listener) {
    mListener = Optional.fromNullable(listener);
    return this;
  }

  public MessageThread getThread(int position) {
    return mThreads.get(position);
  }

  public int indexByUserId(String userId) {
    return mThreadsByUser.containsKey(userId) ? mThreads.indexOf(mThreadsByUser.get(userId)) : NONE;
  }

  @Override
  public void onChildAdded(DataSnapshot dataSnapshot, String s) {
    MessageThread thread = dataSnapshot.getValue(MessageThread.class);
    mThreads.add(thread);
    mThreadsMap.put(thread.getThreadId(), thread);
    mThreadsByUser.put(thread.getPartnerId(), thread);
    notifyDataSetChanged();
  }

  @Override
  public void onChildChanged(DataSnapshot dataSnapshot, String s) {
    // I don't know why I'm doing this, but it just feels right.
    onChildRemoved(dataSnapshot);
    onChildAdded(dataSnapshot, s);
    notifyDataSetChanged();
  }

  @Override
  public void onChildRemoved(DataSnapshot dataSnapshot) {
    MessageThread thread = dataSnapshot.getValue(MessageThread.class);
    if (mThreads.contains(thread)) mThreads.remove(thread);
    if (mThreadsMap.containsValue(thread)) mThreadsMap.remove(thread.getThreadId());
    if (mThreadsByUser.containsValue(thread)) mThreadsMap.remove(thread.getThreadId());
    notifyDataSetChanged();
  }

  @Override
  public void onChildMoved(DataSnapshot dataSnapshot, String s) {
    onChildChanged(dataSnapshot, s);
  }

  @Override
  public void onCancelled(FirebaseError firebaseError) {
    Bugsnag.notify(firebaseError.toException());
    mThreads.clear();
    mThreadsMap.clear();
    mThreadsByUser.clear();
    notifyDataSetChanged();
  }

  public void onThreadClosed(String threadId) {
    MessageThread thread = mThreadsMap.remove(threadId);
    mThreads.remove(thread);
    mThreadsByUser.remove(thread.getPartnerId());
    notifyDataSetChanged();
    mUserThreads.child(threadId).removeValue();
  }

  public static interface OnThreadsUpdatedListener {
    public void onThreadsUpdated(int count);
  }
}