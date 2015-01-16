package com.mhacks.android.ui.common.parse;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;

import com.bugsnag.android.Bugsnag;
import com.google.common.base.Equivalence;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.mhacks.android.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/27/14.
 */
public class ParseAdapter<T extends ParseObject> extends BaseAdapter implements
                                                                     SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG   = "ParseAdapter";
    public static final String ITEMS = "items";

    private final int     mResId;
    private final Context mContext;
    private final LocalFilter mFilter = new LocalFilter();
    private final Object      mLock   = new Object();
    private final ListCallbacks<T> mCallbacks;

    private       ArrayList<T> mItems         = new ArrayList<>();
    private final ArrayList<T> mOriginalItems = new ArrayList<>();

    private Optional<FilterHandler>            mFilterHandler = Optional.absent();
    private Optional<Equivalence<ParseObject>> mSectioning    = Optional.absent();

    private ParseQueryAdapter.QueryFactory<T> mQueryFactory;
    private SwipeRefreshLayout                mLayout;

    private int mCurrentPage    = 0;
    private int mPageSize       = 0;
    private int mNextPageBuffer = 0;

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

    public ParseAdapter(Context context,
                        int resource,
                        ListCallbacks<T> callbacks,
                        ParseQueryAdapter.QueryFactory<T> queryFactory) {
        this(context, resource, callbacks);
        mQueryFactory = queryFactory;
    }

    public ParseAdapter(Context context,
                        int resource,
                        ListCallbacks<T> callbacks,
                        List<T> items,
                        ParseQueryAdapter.QueryFactory<T> queryFactory) {
        this(context, resource, callbacks, items);
        mQueryFactory = queryFactory;
    }

    public ParseAdapter<T> load() {
        if (mQueryFactory == null) {
            clear();
            return null;
        }
        ParseQuery<T> query = mQueryFactory.create();

        if (mPageSize > 0) {
            query.setLimit(mPageSize);
            mCurrentPage = 0;
        }

        Log.d(TAG, "Reloading!");
        query.findInBackground(new FindCallback<T>() {
            @Override
            public void done(List<T> ts, ParseException e) {
                Log.d(TAG, "Done reloading!");
                if (e != null) {
                    e.printStackTrace();
                    return;
                }

                clear();
                mItems.addAll(ts);
                mOriginalItems.clear();
                mOriginalItems.addAll(mItems);
                notifyDataSetChanged();
            }
        });
        return this;
    }

    private void loadPageRelative(int where) {
        mQueryFactory.create()
                     .setLimit(mPageSize)
                     .setSkip((mCurrentPage + where) * mPageSize)
                     .findInBackground(new FindCallback<T>() {
                         @Override
                         public void done(List<T> ts, ParseException e) {
                             if (e != null) {
                                 e.printStackTrace();
                                 Bugsnag.notify(e);
                                 return;
                             }

                             mItems.addAll(ts);
                             mOriginalItems.addAll(ts);
                             notifyDataSetChanged();
                         }
                     });
        mCurrentPage += where;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mResId, null);
        }

        T item = getItem(position);
        T prevItem = position > 0 ? getItem(position - 1) : null;
        T nextItem = position < getCount() - 1 ? getItem(position + 1) : null;

        boolean hasSectionHeader =
                mSectioning.isPresent() &&
                (prevItem != null && !mSectioning.get().equivalent(item, prevItem) ||
                 prevItem == null)
                || !mSectioning.isPresent();
        boolean hasSectionFooter =
                mSectioning.isPresent() &&
                (nextItem != null && !mSectioning.get().equivalent(item, nextItem) ||
                 nextItem == null)
                || !mSectioning.isPresent();

        if (mCallbacks != null) {
            mCallbacks.populateView(ViewHolder.from(view),
                                    item,
                                    hasSectionHeader,
                                    hasSectionFooter);
        }

        if ((!mFilterHandler.isPresent() || !mFilterHandler.get().isFiltering()) && mPageSize > 0) {
            int currentPageHead = mPageSize * mCurrentPage;
            int nextPageHead = currentPageHead + mPageSize;
            int prevPageHead = currentPageHead - mPageSize;
            if (position >= nextPageHead - mNextPageBuffer && mItems.size() <= nextPageHead) {
                loadPageRelative(1);
            }
            else if (position <= prevPageHead - mNextPageBuffer && mCurrentPage > 0 &&
                     mItems.size() <= prevPageHead) {
                loadPageRelative(-1);
            }
        }

        return view;
    }

    public FilterHandler prepareFilter(Menu menu,
                                       int searchId,
                                       boolean withQuery,
                                       String... columns) {
        unloadFilter();
        mFilterHandler = Optional.of(new FilterHandler(menu, searchId, withQuery, columns));
        return mFilterHandler.get();
    }

    public LocalFilter getFilter() {
        return mFilter;
    }

    public void unloadFilter() {
        if (mFilterHandler.isPresent()) {
            mFilterHandler.get().unload();
        }
        mFilterHandler = Optional.absent();
    }

    public ParseAdapter<T> setOrdering(Comparator<T> comparator) {
        if (comparator == null && mItems instanceof SortingArrayList) {
            mItems = new ArrayList<>(mItems);
        }
        else if (comparator != null && !(mItems instanceof SortingArrayList)) {
            mItems = new SortingArrayList<>(mItems, comparator);
        }
        return this;
    }

    public ParseAdapter<T> enablePagination(int pageSize, int nextPageBuffer) {
        mPageSize = pageSize;
        mNextPageBuffer = nextPageBuffer;
        mCurrentPage = 0;
        return this;
    }

    public ParseAdapter<T> disablePagination() {
        mPageSize = mNextPageBuffer = mCurrentPage = 0;
        return this;
    }
/*
    @Override
    public void onSyncError(Synchronize.SyncException e) {
        notifyDataSetChanged();
    }
*/
    public void bindSync(SwipeRefreshLayout layout) {
        mLayout = layout;
        layout.setOnRefreshListener(this);
        layout.setColorSchemeResources(android.R.color.holo_blue_bright,
                                       android.R.color.holo_green_light,
                                       android.R.color.holo_orange_light,
                                       android.R.color.holo_red_light);
    }

    public ParseAdapter<T> setSectioning(Equivalence<ParseObject> equivalence) {
        mSectioning = Optional.fromNullable(equivalence);
        return this;
    }
/*
    @Override
    public void onRefresh() {
        new Synchronization(this, mLayout).execute();
    }
*/
    private void clear() {
        mItems.clear();
    }

    public int indexOf(T t) {
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

    public void onSyncStarted() {
        notifyDataSetChanged();
    }

    public void onSyncCompleted() {
        load();
    }

    @Override
    public void onRefresh() {

    }

    private class FilterHandler implements TextWatcher, MenuItem.OnActionExpandListener {

        private String[] mmColumns;
        private EditText mmEditText;
        private MenuItem mmMenuItem;
        private boolean  mmWithQuery;

        private boolean mmFiltering;

        public FilterHandler(Menu menu, int searchId, boolean withQuery, String... columns) {
            mmColumns = columns;
            mmWithQuery = withQuery;

            mmEditText = (EditText) menu.findItem(searchId).getActionView();
            mmEditText.addTextChangedListener(this);

            mmMenuItem = menu.findItem(R.id.menu_search);
            mmMenuItem.setOnActionExpandListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // nothing to do here
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            mFilter.usingColumns(mmColumns).usingQuery(mmWithQuery).filter(charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // ditto
        }

        @Override
        public boolean onMenuItemActionExpand(MenuItem menuItem) {
            mmEditText.post(new Runnable() {
                @Override
                public void run() {
                    mmEditText.requestFocusFromTouch();
                    InputMethodManager imm =
                            (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mmEditText, InputMethodManager.SHOW_IMPLICIT);
                    mLayout.setEnabled(false);
                }
            });
            mmFiltering = true;
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
            mmEditText.setText(null);
            mmEditText.clearFocus();
            mLayout.setEnabled(true);
            mItems.clear();
            mItems.addAll(mOriginalItems);
            notifyDataSetChanged();

            mmFiltering = false;
            return true;
        }

        public void unload() {
            mmEditText.removeTextChangedListener(this);
            mmMenuItem.setOnActionExpandListener(null);
            mmEditText = null;
            mmMenuItem = null;
            mmFiltering = false;
        }

        public boolean isFiltering() {
            return mmFiltering;
        }
    }

    public class LocalFilter extends Filter {

        private Optional<Predicate<String>> mmPredicate = Optional.absent();
        private String[]                    mmColumns   = null;
        private boolean                     mUsingQuery = false;

        public LocalFilter() {
        }

        public LocalFilter usingColumns(String... columns) {
            mmColumns = columns;
            mmPredicate = Optional.absent();
            return this;
        }

        public LocalFilter usingQuery(boolean usingQuery) {
            mUsingQuery = usingQuery;
            return this;
        }

        public LocalFilter withPredicate(Predicate<String> predicate) {
            mmColumns = null;
            mmPredicate = Optional.fromNullable(predicate);
            return this;
        }

        protected FilterResults performFiltering(final Predicate<T> predicate) {
            if (predicate == null) {
                return performFiltering((CharSequence) null);
            }
            ArrayList<T> list = Lists.newArrayList(Iterables.filter(mOriginalItems, predicate));
            FilterResults results = new FilterResults();
            results.values = list;
            results.count = list.size();
            return results;
        }

        @Override
        protected FilterResults performFiltering(final CharSequence prefix) {
            FilterResults results = new FilterResults();
            List<T> list = new ArrayList<>();

            if (prefix == null || prefix.length() == 0) {
                synchronized (mLock) {
                    list.addAll(mOriginalItems);
                    results.values = list;
                    results.count = list.size();
                }
            }
            else if (mUsingQuery) {
                for (String column : mmColumns) {
                    try {
                        list.addAll(mQueryFactory.create()
                                                 .whereContains(column, prefix.toString())
                                                 .find());
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                        Bugsnag.notify(e);
                    }
                }
            }
            else {
                Predicate<T> predicate = new Predicate<T>() {
                    @Override
                    public boolean apply(T input) {
                        StringBuilder compositeBuilder = new StringBuilder();
                        if (mmColumns != null) {
                            for (String column : mmColumns) {
                                compositeBuilder.append(input.getString(column));
                            }
                        }
                        else {
                            compositeBuilder.append(input.toString());
                        }
                        String composite = compositeBuilder.toString();

                        return (mmPredicate.isPresent() && mmPredicate.get().apply(composite)) ||
                               (composite.contains(prefix));
                    }
                };
                list = Lists.newArrayList(Iterables.filter(mOriginalItems, predicate));
            }
            results.values = list;
            results.count = list.size();

            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            mItems.clear();
            mItems.addAll((List<T>) results.values);
            if (results.count > 0) {
                notifyDataSetChanged();
            }
            else {
                notifyDataSetInvalidated();
            }
        }
    }


    public static interface ListCallbacks<T extends ParseObject> {

        public void populateView(ViewHolder holder,
                                 T t,
                                 boolean hasSectionHeader,
                                 boolean hasSectionFooter);
    }
}
