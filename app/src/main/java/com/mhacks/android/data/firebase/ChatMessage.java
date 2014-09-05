package com.mhacks.android.data.firebase;

import com.firebase.client.Firebase;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.mhacks.android.data.model.User;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/7/14.
 */
public class ChatMessage {
  public static final String MESSAGE = "message";
  public static final String USER = "user";
  public static final String IMAGE = "image";

  public static final Pattern HE_KNOWS = Pattern.compile("(?i)HELL+ *YEAH");

  private String message;
  private String user;
  private String image;

  public static Firebase push(String message, Firebase firebase) {
    Firebase result = firebase.push();
    result.setValue(new ChatMessage(message).toMap());
    return result;
  }

  // Required default constructor for Firebase object mapping
  @SuppressWarnings("unused")
  protected ChatMessage() { }

  ChatMessage(String message, String user, String image) {
    this.message = message;
    this.user = user;
    this.image = image;
  }

  public ChatMessage(String message) {
    User user = User.getCurrentUser();
    this.message = message;

    String name = user.getName();
    this.user = Strings.nullToEmpty(name);

    String imageUrl = user.getImageUrl();
    this.image = Strings.nullToEmpty(imageUrl);
  }

  public String getMessage() {
    return message;
  }

  public String getUser() {
    return user;
  }

  public String getImage() {
    return image;
  }

  public Map<String, Object> toMap() {
    return ImmutableMap.<String, Object>of(
      MESSAGE, message,
      USER, user,
      IMAGE, image
    );
  }

  // determines whether this instance is posted by Dave Fontenot or one of his disciples
  public boolean heKnows() {
    return HE_KNOWS.matcher(message).find();
  }

}
