package com.mhacks.android.ui.chat;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.mhacks.android.data.firebase.ChatMessage;
import com.mhacks.android.data.firebase.ChatRoom;
import com.mhacks.android.ui.common.CircleTransform;
import com.mhacks.android.ui.common.FirebaseListAdapter;
import com.mhacks.iv.android.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/24/14.
 */
public class ChatFragment extends Fragment implements ActionBar.OnNavigationListener, View.OnClickListener {
  public static final String TAG = "ChatFragment";

  public static final String CHAT = "chat";
  public static final String ROOMS = "rooms";
  public static final String MESSAGES = "messages";

  private String mFirebaseUrl;
  private Firebase mFirebase;
  private Firebase mMessages;
  private Firebase mCurrentRoom;
  private RelativeLayout mLayout;
  private ListView mListView;
  private RoomsAdapter mRoomsAdapter;
  private ChatAdapter mChatAdapter;
  private EditText mInput;
  private ImageButton mSendButton;

  public ChatFragment() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    mFirebaseUrl = getString(R.string.firebase_url);
    mFirebase = new Firebase(mFirebaseUrl).child(CHAT);
    mMessages = mFirebase.child(MESSAGES);
    mRoomsAdapter = new RoomsAdapter(mFirebase.child(ROOMS).limit(50), getActivity());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_chat, null);
    mListView = (ListView) mLayout.findViewById(R.id.chat_list);
    mInput = (EditText) mLayout.findViewById(R.id.chat_input);
    mSendButton = (ImageButton) mLayout.findViewById(R.id.chat_send_button);

    return mLayout;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mSendButton.setOnClickListener(this);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    if (User.canAdmin()) {
      inflater.inflate(R.menu.fragment_chat, menu);
    }
    setNavigationMode();
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.chat_new:
        // TODO: do stuff
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setNavigationMode() {
    ActionBar actionBar = getActivity().getActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    actionBar.setListNavigationCallbacks(mRoomsAdapter, this);
    actionBar.setDisplayShowTitleEnabled(false);
  }

  @Override
  public boolean onNavigationItemSelected(int i, long l) {
    mCurrentRoom = mMessages.child(mRoomsAdapter.getItem(i).getTitle());
    mChatAdapter = new ChatAdapter(mCurrentRoom.limit(50));
    mListView.setAdapter(mChatAdapter);

    return true;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mRoomsAdapter.cleanup();
    if (mChatAdapter != null) mChatAdapter.cleanup();
  }

  @Override
  public void onClick(View view) {
    CharSequence text = mInput.getText();
    if (text.length() > 0) {
      ChatMessage.push(text.toString(), mCurrentRoom);
      mInput.setText(null);
    }
  }

  private static class RoomsAdapter extends FirebaseListAdapter<ChatRoom> {

    public RoomsAdapter(Query ref, Activity activity) {
      super(ref, ChatRoom.class, android.R.layout.simple_spinner_dropdown_item, activity);
    }

    @Override
    protected void populateView(ViewHolder holder, ChatRoom chatRoom) {
      TextView title = holder.get(android.R.id.text1);
      title.setText(chatRoom.getTitle());
    }

  }

  private class ChatAdapter extends FirebaseListAdapter<ChatMessage> {

    public ChatAdapter(Query ref) {
      super(ref, ChatMessage.class, R.layout.adapter_chat_message, getActivity());
    }

    @Override
    protected void populateView(ViewHolder holder, ChatMessage message) {
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

}
