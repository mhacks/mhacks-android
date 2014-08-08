package com.mhacks.android.data.firebase;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/7/14.
 */
public class ChatMessage {

  private String message;
  private String author;

  // Required default constructor for Firebase object mapping
  @SuppressWarnings("unused")
  private ChatMessage() { }

  ChatMessage(String message, String author) {
    this.message = message;
    this.author = author;
  }

  public String getMessage() {
    return message;
  }

  public String getAuthor() {
    return author;
  }

}
