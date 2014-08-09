package com.mhacks.android.ui.common;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bugsnag.android.Bugsnag;
import com.mhacks.android.data.sync.Synchronization;
import com.mhacks.android.data.sync.Synchronize;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/27/14.
 */
public class ParseAdapter<T extends ParseObject> extends BaseAdapter implements Synchronization.SyncCallbacks, SwipeRefreshLayout.OnRefreshListener {
  public static final String TAG = "ParseAdapter";
  public static final String ITEMS = "items";

  private final int mResId;
  private final Context mContext;
  private final ListCallbacks<T> mCallbacks;
  private final ArrayList<T> mItems = new ArrayList<>();
  private ParseQueryAdapter.QueryFactory<T> mQueryFactory;
  private SwipeRefreshLayout mLayout;

  public ParseAdapter(Context context, int resource, ListCallbacks<T> callbacks) {
    mContext = context;
    mResId = resource;
    mCallbacks = callbacks;
    mQueryFactory = null;
  }

  public ParseAdapter(Context context, int resource, ListCallbacks<T> callbacks, List<T> items) {
    this(context, resource, callbacks);
    mItems.addAll(items);
  }

  public ParseAdapter(Context context, int resource, ListCallbacks<T> callbacks, ParseQueryAdapter.QueryFactory<T> queryFactory) {
    this(context, resource, callbacks);
    mQueryFactory = queryFactory;
    load();
  }

  public ParseAdapter(Context context, int resource, ListCallbacks<T> callbacks, List<T> items, ParseQueryAdapter.QueryFactory<T> queryFactory) {
    this(context, resource, callbacks, items);
    mQueryFactory = queryFactory;
    load();
  }

  public ParseQueryAdapter.QueryFactory<T> load() {
    if (mQueryFactory == null) {
      clear();
      return null;
    }
    mQueryFactory.create().findInBackground(new FindCallback<T>() {
      @Override
      public void done(List<T> ts, ParseException e) {
        if (e != null) {
          e.printStackTrace();
          Bugsnag.notify(e);
          return;
        }
        clear();
        mItems.addAll(ts);
        notifyDataSetChanged();
      }
    });
    return mQueryFactory;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    if (view == null) {
      LayoutInflater inflater = LayoutInflater.from(mContext);
      view = inflater.inflate(mResId, null);
    }

    if (mCallbacks != null) mCallbacks.fillView(ViewHolder.from(view), getItem(position));

    return view;
  }

  @Override
  public void onSyncError(Synchronize.SyncException e) {
    load();
  }

  public void bindSync(SwipeRefreshLayout layout) {
    mLayout = layout;
    layout.setOnRefreshListener(this);
    layout.setColorSchemeResources(android.R.color.holo_blue_bright,
      android.R.color.holo_green_light,
      android.R.color.holo_orange_light,
      android.R.color.holo_red_light);
  }

  @Override
  public void onRefresh() {
    new Synchronization(this, mLayout).execute();
  }

  private void clear() {
    mItems.clear();
  }

  public int positionOf(T t) {
    return mItems.indexOf(t);
  }

  public void load(ParseQueryAdapter.QueryFactory<T> queryFactory) {
    mQueryFactory = queryFactory;
    load();
  }

  public ParseQueryAdapter.QueryFactory<T> getQueryFactory() {
    return mQueryFactory;
  }

  public int getResId() {
    return mResId;
  }

  @Override
  public int getCount() {
    return mItems.size();
  }

  @Override
  public T getItem(int i) {
    return mItems.get(i);
  }

  public ArrayList<T> getItems() {
    return mItems;
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public void onSyncStarted() {
    load();
  }

  @Override
  public void onSyncCompleted() {
    load();
  }

  public static class ViewHolder {
    public final View root;
    private final Map<Integer, View> mMap = new ConcurrentHashMap<>();

    private ViewHolder(View root) {
      this.root = root;
      this.root.setTag(this);
    }

    public static ViewHolder from(View view) {
      ViewHolder holder = (ViewHolder) view.getTag();
      if (holder == null) {
        view.setTag(holder = new ViewHolder(view));
      }
      return holder;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T get(int id) {
      if (!mMap.containsKey(id)) {
        mMap.put(id, root.findViewById(id));
      }
      return (T) mMap.get(id);
    }
  }

  public static interface ListCallbacks<T extends ParseObject> {
    public void fillView(ViewHolder holder, T t);
  }
}
