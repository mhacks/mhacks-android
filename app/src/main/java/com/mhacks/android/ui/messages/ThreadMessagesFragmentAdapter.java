package com.mhacks.android.ui.messages;

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
  ChildEventListener, ThreadMessagesFragment.OnThreadClosedListener {

  private final Firebase mUserThreads;
  private final Firebase mMessages;
  private final List<MessageThread> mThreads = new ArrayList<>();
  private final Map<String, MessageThread> mThreadsMap = Maps.newHashMap();
  private Optional<OnThreadsUpdatedListener> mListener = Optional.absent();

  public ThreadMessagesFragmentAdapter(FragmentManager fm, Firebase userThreads, Firebase messages) {
    super(fm);
    mUserThreads = userThreads;
    mMessages = messages;
    mUserThreads.addChildEventListener(this);
  }

  public void cleanup() {
    mUserThreads.removeEventListener(this);
    mThreads.clear();
    mThreadsMap.clear();
  }

  @Override
  public Fragment getItem(int position) {
    return ThreadMessagesFragment.newInstance(mMessages.child(mThreads.get(position).getThreadId()), this);
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

  @Override
  public void onChildAdded(DataSnapshot dataSnapshot, String s) {
    MessageThread thread = dataSnapshot.getValue(MessageThread.class);
    mThreads.add(thread);
    mThreadsMap.put(thread.getThreadId(), thread);
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
    notifyDataSetChanged();
  }

  @Override
  public void onThreadClosed(String threadId) {
    mThreads.remove(mThreadsMap.remove(threadId));
    notifyDataSetChanged();
    mUserThreads.child(threadId).removeValue();
  }

  public static interface OnThreadsUpdatedListener {
    public void onThreadsUpdated(int count);
  }
}