package com.mhacks.android.data.firebase;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/7/14.
 */
public class ChatRoom {

  private String title;
  private String details;

  @SuppressWarnings("unused")
  private ChatRoom() { }

  ChatRoom(String title, String details) {
    this.title = title;
    this.details = details;
  }

  public String getTitle() {
    return title;
  }

  public String getDetails() {
    return details;
  }

}
