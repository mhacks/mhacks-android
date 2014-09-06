package com.mhacks.android.ui.nav;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mhacks.iv.android.R;

import java.util.List;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/27/14.
 */
public class NavItemAdapter extends ArrayAdapter<NavItem> {

  private final Animation mSlideInAnimation;
  private final Animation mSlideOutAnimation;

  private int mSelectedItemPosition = 0;
  private int mDeselectedItemPosition = -1;

  public NavItemAdapter(Context context, List<NavItem> items) {
    super(context, R.layout.adapter_nav_item, items);
    mSlideInAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
    mSlideOutAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out_left);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder = getHolder(getContext(), convertView, parent);
    NavItem item = getItem(position);

    holder.title.setText(item.getTitle());
    holder.icon.setImageResource(item.getIconId());

    holder.background.setColorFilter(item.getColorFilter());

    if (position == mSelectedItemPosition) {
      holder.backgroundView.startAnimation(mSlideInAnimation);
      holder.backgroundView.setVisibility(View.VISIBLE);
    } else if (position == mDeselectedItemPosition) {
      holder.backgroundView.startAnimation(mSlideOutAnimation);
      mDeselectedItemPosition = -1;
      holder.backgroundView.setVisibility(View.INVISIBLE);
    } else {
      holder.backgroundView.setVisibility(View.INVISIBLE);
    }

    return holder.root;
  }

  private static ViewHolder getHolder(Context context, View view, ViewGroup parent) {
    if (view == null) {
      LayoutInflater inflater = LayoutInflater.from(context);
      view = inflater.inflate(R.layout.adapter_nav_item, null);
      view.setTag(new ViewHolder(view));
    }
    return (ViewHolder) view.getTag();
  }

  public void setSelection(int position) {
    mDeselectedItemPosition = mSelectedItemPosition;
    mSelectedItemPosition = position;
  }

  private static class ViewHolder {
    public final ImageView icon;
    public final TextView title;
    public final View root;
    public final Drawable background;
    public final View backgroundView;

    public ViewHolder(View root) {
      this.root = root;
      this.icon = (ImageView) root.findViewById(R.id.nav_item_icon);
      this.title = (TextView) root.findViewById(R.id.nav_item_title);
      this.backgroundView = root.findViewById(R.id.nav_item_background);
      this.background = ((LayerDrawable) this.backgroundView
        .getBackground())
        .findDrawableByLayerId(R.id.bg_nav_item_shape);
    }
  }

}