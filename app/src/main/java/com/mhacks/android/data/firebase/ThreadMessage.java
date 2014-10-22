package com.mhacks.android.data.firebase;

import com.firebase.client.Firebase;
import com.google.common.collect.ImmutableMap;
import com.mhacks.android.ui.messages.ThreadsFragment;

import java.util.Map;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/7/14.
 */
public final class ThreadMessage extends ChatMessage {

  public static final String THREAD_ID = "threadId";

  public String threadId;

  @SuppressWarnings("unused")
  protected ThreadMessage() { }

  public static Firebase push(String message, MessageThread thread, Firebase priv) {
    String threadId = thread.getThreadId();
    String partnerId = thread.getPartnerId();

    Firebase threadMessages = priv.child(ThreadsFragment.MESSAGES).child(threadId);
    Firebase partnerThreads = priv.child(ThreadsFragment.THREADS).child(partnerId);

    // Update the partner's threads, if they've removed it
    User currentUser = User.getCurrentUser();
    partnerThreads.child(threadId).setValue(new MessageThread(threadId, currentUser.getName(), currentUser.getObjectId()).toMap());

    Firebase result = threadMessages.push();
    result.setValue(new ThreadMessage(message, threadId).toMap());
    return result;
  }

  public ThreadMessage(String message, String threadId) {
    super(message);
    this.threadId = threadId;
  }

  @Override
  public Map<String, Object> toMap() {
    return ImmutableMap.<String, Object>builder()
      .putAll(super.toMap())
      .put(THREAD_ID, threadId)
      .build();
  }
}
