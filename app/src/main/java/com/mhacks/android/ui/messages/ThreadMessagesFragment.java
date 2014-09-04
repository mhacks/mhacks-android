package com.mhacks.android.ui.messages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.mhacks.android.R;
import com.mhacks.android.data.firebase.ThreadMessage;
import com.mhacks.android.ui.common.CircleTransform;
import com.mhacks.android.ui.common.FirebaseListAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/24/14.
 */
public class ThreadMessagesFragment extends Fragment {
  public static final String TAG = "ThreadMessagesFragment";

  private RelativeLayout mLayout;
  private Firebase mThreadMessages;
  private ListView mListView;
  private MessagesAdapter mMessagesAdapter;
  private OnThreadClosedListener mListener;

  public static ThreadMessagesFragment newInstance(Firebase threadMessages, OnThreadClosedListener listener) {
    ThreadMessagesFragment result = new ThreadMessagesFragment();
    result.mThreadMessages = threadMessages;
    result.mListener = listener;
    return result;
  }

  public ThreadMessagesFragment() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    mMessagesAdapter = new MessagesAdapter(mThreadMessages);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_thread_messages, container, false);
    mListView = (ListView) mLayout.findViewById(R.id.messages_list);

    return mLayout;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mListView.setAdapter(mMessagesAdapter);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.fragment_thread_messages, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    mListener.onThreadClosed(mThreadMessages.getName());
    return true;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mMessagesAdapter.cleanup();
  }

  private class MessagesAdapter extends FirebaseListAdapter<ThreadMessage> {

    public MessagesAdapter(Query ref) {
      super(ref, ThreadMessage.class, R.layout.adapter_chat_message, getActivity());
    }

    @Override
    protected void populateView(ViewHolder holder, ThreadMessage message) {
      ImageView dave = holder.get(R.id.dave);
      ImageView image = holder.get(R.id.chat_message_image);
      TextView name = holder.get(R.id.chat_message_user_name);
      TextView text = holder.get(R.id.chat_message_text);

      dave.setVisibility(message.heKnows() ? View.VISIBLE : View.INVISIBLE);

      Picasso.with(getActivity())
        .load(message.getImage())
        .transform(new CircleTransform())
        .into(image);

      name.setText(message.getUser());
      text.setText(message.getMessage());
    }

  }

  public static interface OnThreadClosedListener {
    public void onThreadClosed(String threadId);
  }

}
