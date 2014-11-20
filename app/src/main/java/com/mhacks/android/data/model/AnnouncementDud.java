package com.mhacks.android.data.model;

import com.parse.ParseObject;

/**
 * Created by jawad on 19/11/14.
 */
public class AnnouncementDud {
    private String author;
    private String date;
    private String title;
    private String message;

    public AnnouncementDud(ParseObject announcement) {
        // Get the data from the parseObject first, only for readability and sanity
        String author = announcement.getString(Announcement.AUTHOR_COL);
        String date = announcement.getString(Announcement.DATE_COL);
        String title = announcement.getString(Announcement.TITLE_COL);
        String message = announcement.getString(Announcement.MESSAGE_COL);

        // Set the data
        setAuthor(author);
        setDate(date);
        setTitle(title);
        setMessage(message);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
