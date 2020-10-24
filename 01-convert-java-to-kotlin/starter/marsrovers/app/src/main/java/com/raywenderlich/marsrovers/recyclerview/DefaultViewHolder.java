package com.raywenderlich.marsrovers.recyclerview;


import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * A default view holder that makes it easy to find views
 */
public class DefaultViewHolder extends RecyclerView.ViewHolder {
	protected Map<Integer, View> viewMap = new HashMap<>();

	public DefaultViewHolder(View itemView) {
		super(itemView);
		findViewItems(itemView);
	}

	public View getView(int id) {
		return viewMap.get(id);
	}

	/**
	 * Set the string for the given textView id
	 * @param id
	 * @param text
	 */
	public void setText(int id, String text) {
		final View view = viewMap.get(id);
		if (view == null) {
			throw new IllegalArgumentException("View for " + id + " not found");
		}
		if (!(view instanceof TextView)) {
			throw new IllegalArgumentException("View for " + id + " is not a TextView");
		}
		((TextView)view).setText(text);
	}

	/**
	 * Set the string for the given textView id
	 * @param id
	 * @param textRes
     */
	public void setText(int id, @StringRes int textRes) {
		final View view = viewMap.get(id);
		if (view == null) {
			throw new IllegalArgumentException("View for " + id + " not found");
		}
		if (!(view instanceof TextView)) {
			throw new IllegalArgumentException("View for " + id + " is not a TextView");
		}
		((TextView)view).setText(textRes);
	}

	/**
	 * Set the checkbox checked
	 * @param id
	 * @param checked
	 */
	public void setChecked(int id, boolean checked) {
		final View view = viewMap.get(id);
		if (view == null) {
			throw new IllegalArgumentException("View for " + id + " not found");
		}
		if (!(view instanceof CompoundButton)) {
			throw new IllegalArgumentException("View for " + id + " is not a CompoundButton");
		}
		((CompoundButton)view).setChecked(checked);
	}

	/**
	 * Check if the checkbox is checked
	 * @param id
	 * @return
	 */
	public boolean isChecked(int id) {
		final View view = viewMap.get(id);
		if (view == null) {
			throw new IllegalArgumentException("View for " + id + " not found");
		}
		if (!(view instanceof CompoundButton)) {
			throw new IllegalArgumentException("View for " + id + " is not a CompoundButton");
		}
		return ((CompoundButton)view).isChecked();
	}

	/**
	 * Get the text value for the given id
	 * @param id
	 * @return
	 */
	public String getText(int id) {
		final View view = viewMap.get(id);
		if (view == null) {
			throw new IllegalArgumentException("View for " + id + " not found");
		}
		if (!(view instanceof TextView)) {
			throw new IllegalArgumentException("View for " + id + " is not a TextView");
		}
		return ((TextView)view).getText().toString();
	}

	/**
	 * Set the ImageView with the given id
	 * @param id
	 * @param drawable
	 * @return ImageView
	 */
	public void setImage(int id, @DrawableRes int drawable) {
		final View view = viewMap.get(id);
		if (view == null) {
			throw new IllegalArgumentException("View for " + id + " not found");
		}
		if (!(view instanceof ImageView)) {
			throw new IllegalArgumentException("View for " + id + " is not a ImageView");
		}
		((ImageView)view).setImageResource(drawable);
	}

	/**
	 * Set the ImageView with the given id
	 * @param id
	 * @param drawable
	 * @return ImageView
	 */
	public void setImage(int id, Drawable drawable) {
		final View view = viewMap.get(id);
		if (view == null) {
			throw new IllegalArgumentException("View for " + id + " not found");
		}
		if (!(view instanceof ImageView)) {
			throw new IllegalArgumentException("View for " + id + " is not a ImageView");
		}
		((ImageView)view).setImageDrawable(drawable);
	}

	/**
	 * Set a view visible or not
	 * @param id
	 * @param visible
	 */
	public void setVisible(int id, boolean visible) {
		final View view = viewMap.get(id);
		if (view == null) {
			throw new IllegalArgumentException("View for " + id + " not found");
		}
		view.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	/**
	 * Set a click listener
	 * @param id
	 * @param clickListener
     */
	public void setClickListener(int id, View.OnClickListener clickListener) {
		final View view = viewMap.get(id);
		if (view == null) {
			throw new IllegalArgumentException("View for " + id + " not found");
		}
		view.setOnClickListener(clickListener);
	}

	/**
	 * Set a click listener on the whole view
	 * @param clickListener
	 */
	public void setClickListener(View.OnClickListener clickListener) {
		itemView.setOnClickListener(clickListener);
	}

	/**
	 * Get the ImageView with the given id
	 * @param id
	 * @return ImageView
     */
	public ImageView getImage(int id) {
		final View view = viewMap.get(id);
		if (view == null) {
			throw new IllegalArgumentException("View for " + id + " not found");
		}
		if (!(view instanceof ImageView)) {
			throw new IllegalArgumentException("View for " + id + " is not a ImageView");
		}
		return ((ImageView)view);
	}

	/**
	 * Find all views and put them in a map
	 * @param itemView
     */
	protected void findViewItems(View itemView) {
		if (itemView instanceof ViewGroup) {
			if (itemView.getId() != View.NO_ID) {
				viewMap.put(itemView.getId(), itemView);
			}
			ViewGroup viewGroup = (ViewGroup)itemView;
			final int childCount = viewGroup.getChildCount();
			for (int i = 0; i < childCount; i++) {
				final View childView = viewGroup.getChildAt(i);
				if (childView.getId() != View.NO_ID) {
					viewMap.put(childView.getId(), childView);
				}
				if (childView instanceof ViewGroup) {
					findViewItems(childView);
				}
			}
		} else {
			viewMap.put(itemView.getId(), itemView);
		}
	}
}
