package com.mhacks.android.data.firebase;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/7/14.
 */
public class ChatMessage {

  private String message;
  private String user;
  private String image;

  // Required default constructor for Firebase object mapping
  @SuppressWarnings("unused")
  private ChatMessage() { }

  ChatMessage(String message, String user, String image) {
    this.message = message;
    this.user = user;
    this.image = image;
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

}
