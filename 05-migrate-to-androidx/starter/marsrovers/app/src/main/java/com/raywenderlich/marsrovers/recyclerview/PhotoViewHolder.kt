package com.raywenderlich.marsrovers.recyclerview

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import java.util.*

typealias PhotoClickListener = ((View) -> Unit)?
/**
 * A default view holder that makes it easy to find views
 */
class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected var photoMap: MutableMap<Int, View> = HashMap()
    fun getView(id: Int): View? {
        return photoMap[id]
    }

    /**
     * Set the string for the given textView id
     * @param id
     * @param text
     */
    fun setText(id: Int, text: String?) {
        val view = photoMap[id] ?: throw IllegalArgumentException("View for $id not found")
        require(view is TextView) { "View for $id is not a TextView" }
        view.text = text
    }

    /**
     * Set the string for the given textView id
     * @param id
     * @param textRes
     */
    fun setText(id: Int, @StringRes textRes: Int) {
        val view = photoMap[id] ?: throw IllegalArgumentException("View for $id not found")
        require(view is TextView) { "View for $id is not a TextView" }
        view.setText(textRes)
    }

    /**
     * Set the checkbox checked
     * @param id
     * @param checked
     */
    fun setChecked(id: Int, checked: Boolean) {
        val view = photoMap[id] ?: throw IllegalArgumentException("View for $id not found")
        require(view is CompoundButton) { "View for $id is not a CompoundButton" }
        view.isChecked = checked
    }

    /**
     * Check if the checkbox is checked
     * @param id
     * @return
     */
    fun isChecked(id: Int): Boolean {
        val view = photoMap[id] ?: throw IllegalArgumentException("View for $id not found")
        require(view is CompoundButton) { "View for $id is not a CompoundButton" }
        return view.isChecked
    }

    /**
     * Get the text value for the given id
     * @param id
     * @return
     */
    fun getText(id: Int): String {
        val view = photoMap[id] ?: throw IllegalArgumentException("View for $id not found")
        require(view is TextView) { "View for $id is not a TextView" }
        return view.text.toString()
    }

    /**
     * Set the ImageView with the given id
     * @param id
     * @param drawable
     * @return ImageView
     */
    fun setImage(id: Int, @DrawableRes drawable: Int) {
        val view = photoMap[id] ?: throw IllegalArgumentException("View for $id not found")
        require(view is ImageView) { "View for $id is not a ImageView" }
        view.setImageResource(drawable)
    }

    /**
     * Set the ImageView with the given id
     * @param id
     * @param drawable
     * @return ImageView
     */
    fun setImage(id: Int, drawable: Drawable?) {
        val view = photoMap[id] ?: throw IllegalArgumentException("View for $id not found")
        require(view is ImageView) { "View for $id is not a ImageView" }
        view.setImageDrawable(drawable)
    }

    /**
     * Set a view visible or not
     * @param id
     * @param visible
     */
    fun setVisible(id: Int, visible: Boolean) {
        val view = photoMap[id] ?: throw IllegalArgumentException("View for $id not found")
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    /**
     * Set a click listener
     * @param id
     * @param clickListener
     */
    fun setClickListener(id: Int, clickListener: PhotoClickListener) {
        val view = photoMap[id] ?: throw IllegalArgumentException("View for $id not found")
        view.setOnClickListener(clickListener)
    }

    /**
     * Set a click listener on the whole view
     * @param clickListener
     */
    fun setClickListener(clickListener: View.OnClickListener?) {
        itemView.setOnClickListener(clickListener)
    }

    /**
     * Get the ImageView with the given id
     * @param id
     * @return ImageView
     */
    fun getImage(id: Int): ImageView {
        val view = photoMap[id] ?: throw IllegalArgumentException("View for $id not found")
        require(view is ImageView) { "View for $id is not a ImageView" }
        return view
    }

    /**
     * Find all views and put them in a map
     * @param itemView
     */
    protected fun findViewItems(itemView: View) {
        if (itemView is ViewGroup) {
            if (itemView.getId() != View.NO_ID) {
                photoMap[itemView.getId()] = itemView
            }
            val viewGroup = itemView
            val childCount = viewGroup.childCount
            for (i in 0 until childCount) {
                val childView = viewGroup.getChildAt(i)
                if (childView.id != View.NO_ID) {
                    photoMap[childView.id] = childView
                }
                (childView as? ViewGroup)?.let { findViewItems(it) }
            }
        } else {
            photoMap[itemView.id] = itemView
        }
    }

    init {
        findViewItems(itemView)
    }
}