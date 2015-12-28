package com.mhacks.android.data.network;

/**
 * Created by boztalay on 6/5/15.
 */
public interface HackathonCallback<T> {
    public void success(T response);
    public void failure(Throwable error);
}
