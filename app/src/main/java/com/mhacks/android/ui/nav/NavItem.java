package com.mhacks.android.ui.nav;

import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.bugsnag.android.Bugsnag;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/27/14.
 */
public class NavItem {
  private final FragmentManager mFragmentManager;
  private final Class<? extends Fragment> mClazz;
  private final String mTitle;
  private final int mIconId;
  private final String mTag;
  private final int mColor;
  private final LightingColorFilter mColorFilter;

  public NavItem(FragmentActivity activity, Class<? extends Fragment> clazz, String title, int iconId, int colorId, String tag) {
    mFragmentManager = activity.getSupportFragmentManager();
    mClazz = clazz;
    mTitle = title;
    mIconId = iconId;
    mTag = tag;
    mColor = activity.getResources().getColor(colorId);
    mColorFilter = new LightingColorFilter(0, mColor);
    getFragment();
  }

  public Fragment getFragment() {
    Fragment fragment = mFragmentManager.findFragmentByTag(mTag);
    if (fragment == null) {
      try {
        fragment = mClazz.getConstructor().newInstance();
      } catch (Exception e) {
        e.printStackTrace();
        Bugsnag.notify(e);
      }
    }
    return fragment;
  }

  public int replace(int resId, Bundle args) {
    Fragment fragment = getFragment();
    if (!fragment.isAdded()) fragment.setArguments(args);
    return mFragmentManager.beginTransaction()
      .replace(resId, fragment, mTag)
      .commit();
  }

  public void setHasOptionsMenu(boolean hasOptionsMenu) {
    getFragment().setHasOptionsMenu(hasOptionsMenu);
  }

  public String getTag() {
    return mTag;
  }

  public String getTitle() {
    return mTitle;
  }

  public int getIconId() {
    return mIconId;
  }

  public int getColor() {
    return mColor;
  }

  public LightingColorFilter getColorFilter() {
    return mColorFilter;
  }
}
