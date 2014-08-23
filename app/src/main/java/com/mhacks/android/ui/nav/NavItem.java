package com.mhacks.android.ui.nav;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.LightingColorFilter;
import android.os.Bundle;

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

  private Fragment mFragment;

  public NavItem(Activity activity, Class<? extends Fragment> clazz, String title, int iconId, int colorId, String tag) {
    mFragmentManager = activity.getFragmentManager();
    mClazz = clazz;
    mTitle = title;
    mIconId = iconId;
    mTag = tag;
    mColor = activity.getResources().getColor(colorId);
    mColorFilter = new LightingColorFilter(0, mColor);
    getFragment();
  }

  public NavItem(Activity activity, Class<? extends Fragment> clazz, String title, int iconId, int colorId, String tag, Fragment fragment) {
    this(activity, clazz, title, iconId, colorId, tag);
    mFragment = fragment;
  }

  public Fragment getFragment() {
    if (mFragment == null) {
      mFragment = mFragmentManager.findFragmentByTag(mTag);
      if (mFragment == null) {
        try {
          mFragment = mClazz.getConstructor().newInstance();
        } catch (Exception e) {
          e.printStackTrace();
          Bugsnag.notify(e);
        }
      }
    }
    return mFragment;
  }

  public int replace(int resId, Bundle args) {
    Fragment fragment = getFragment();
    if (!fragment.isAdded()) fragment.setArguments(args);
    return mFragmentManager.beginTransaction()
      .replace(resId, fragment, mTag)
      .commit();
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
