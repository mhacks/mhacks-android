package com.mhacks.android.data.firebase;

import com.firebase.client.Firebase;
import com.google.common.collect.ImmutableMap;
import com.mhacks.android.data.model.User;
import com.mhacks.android.ui.messages.ThreadsFragment;

import java.util.Map;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/7/14.
 */
public final class MessageThread {
  public static final String THREAD_ID = "threadId";
  public static final String PARTNER_ID = "partnerId";
  public static final String PARTNER_NAME = "partnerName";

  private String threadId;
  private String partnerId;
  private String partnerName;

  public static String push(User partner, Firebase priv) {
    User user = User.getCurrentUser();

    Firebase threads = priv.child(ThreadsFragment.THREADS);
    Firebase userThreads = threads.child(user.getUsername());

    Firebase userResult = userThreads.push();
    String threadId = userResult.getName();

    userResult.updateChildren(new MessageThread(threadId, partner.getName(), partner.getUsername()).toMap());

    return threadId;
  }

  // Required default constructor for Firebase object mapping
  @SuppressWarnings("unused")
  private MessageThread() { }

  MessageThread(String threadId, String partnerName, String partnerId) {
    this.threadId = threadId;
    this.partnerId = partnerId;
    this.partnerName = partnerName;
  }

  public String getThreadId() {
    return threadId;
  }

  public String getPartnerName() {
    return partnerName;
  }
  public String getPartnerId() {
    return partnerId;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof MessageThread && ((MessageThread) o).threadId.equals(threadId);
  }

  public Map<String, Object> toMap() {
    return ImmutableMap.<String, Object>of(
      THREAD_ID, threadId,
      PARTNER_ID, partnerId,
      PARTNER_NAME, partnerName
    );
  }

}
