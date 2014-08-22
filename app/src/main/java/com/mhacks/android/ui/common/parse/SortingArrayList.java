package com.mhacks.android.ui.common.parse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/20/14.
 */
public class SortingArrayList<T> extends ArrayList<T> {

  private final Comparator<T> mComparator;

  public SortingArrayList(int capacity, Comparator<T> comparator) {
    super(capacity);
    mComparator = comparator;
  }

  public SortingArrayList(Comparator<T> comparator) {
    super();
    mComparator = comparator;
  }

  public SortingArrayList(Collection<? extends T> collection, Comparator<T> comparator) {
    super(collection);
    mComparator = comparator;
    sort();
  }

  @Override
  public boolean add(T object) {
    return super.add(object) && sort();
  }

  @Override
  public void add(int index, T object) {
    super.add(index, object);
    sort();
  }

  @Override
  public boolean addAll(Collection<? extends T> collection) {
    return super.addAll(collection) && sort();
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> collection) {
    return super.addAll(index, collection) && sort();
  }

  public boolean sort() {
    Collections.sort(this, mComparator);
    return true;
  }

}
