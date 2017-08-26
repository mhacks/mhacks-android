package com.mhacks.android.data.network;

/**
 * Created by boztalay on 6/5/15.
 */
public interface HackathonCallback<T> {
    void success(T response);
    void failure(Throwable error);
}
