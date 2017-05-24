package com.mhacks.android.ui.common;

import android.os.AsyncTask;

import com.google.common.base.Function;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/29/14.
 */
abstract class AsyncFunction<F, T> implements Function<F, T> {

    public static <F, T> AsyncFunction<F, T> from(final Function<F, T> function) {
        return new AsyncFunction<F, T>() {
            @Override
            public T apply(F input) {
                return function.apply(input);
            }
        };
    }

    public void apply(F input, final FunctionCallback<T> callback) {
        AsyncTask<F, Void, T> task = new ApplicatorTask() {
            @Override
            protected void onPostExecute(T t) {
                callback.onResult(t);
            }
        };
        task.execute(input);
    }

    @Override
    public abstract T apply(F input);

    public interface FunctionCallback<T> {

        void onResult(T t);
    }

    private class ApplicatorTask extends AsyncTask<F, Void, T> {

        @SafeVarargs
        @Override
        protected final T doInBackground(F... fs) {
            return apply(fs[0]);
        }
    }

}
