package com.mhacks.android.ui.nav;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/27/14.
 */
public class NavItem {
  private final FragmentManager mFragmentManager;
  private final Class<? extends Fragment> mClazz;
  private final String mTitle;
  private final int mIconId;
  private final String mTag;

  private Fragment mFragment;

  public NavItem(FragmentManager fm, Class<? extends Fragment> clazz, String title, int iconId, String tag) {
    mFragmentManager = fm;
    mClazz = clazz;
    mTitle = title;
    mIconId = iconId;
    mTag = tag;
    getFragment();
  }

  public NavItem(FragmentManager fm, Class<? extends Fragment> clazz, String title, int iconId, String tag, Fragment fragment) {
    this(fm, clazz, title, iconId, tag);
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
}
