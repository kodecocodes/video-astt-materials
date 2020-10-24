package com.raywenderlich.marsrovers.recyclerview

import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.raywenderlich.marsrovers.R
import com.raywenderlich.marsrovers.models.PhotoRow
import com.raywenderlich.marsrovers.models.RowType


/**
 * Adapter for the list of photos
 */
class PhotoAdapter(private var photoList: ArrayList<PhotoRow>) : androidx.recyclerview.widget.RecyclerView.Adapter<PhotoViewHolder>() {

    private var filteredPhotoList = ArrayList<PhotoRow>()
    private var filtering = false

    override fun getItemCount(): Int {
        if (filtering) {
            return filteredPhotoList.size
        }
        return photoList.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoRow: PhotoRow = getPhotoRow(position)
        if (photoRow.type == RowType.PHOTO) {
            holder.setClickListener(R.id.camera_image) {
                
            }
            val photo = photoRow.photo
            Glide.with(holder.itemView.context)
                .load(photo?.img_src)
                .into(holder.getImage(R.id.camera_image))
            photo?.earth_date?.let { holder.setText(R.id.date, it) }
        } else {
            photoRow.header?.let { holder.setText(R.id.header_text, it) }
        }
        setAnimation(holder.itemView)
    }

    private fun getPhotoRow(position: Int): PhotoRow {
        val photoRow: PhotoRow = if (filtering) {
            filteredPhotoList[position]
        } else {
            photoList[position]
        }
        return photoRow
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val inflatedView : View = when (viewType) {
            RowType.PHOTO.ordinal -> layoutInflater.inflate(R.layout.row_item, parent,false)
            else -> layoutInflater.inflate(R.layout.header_item, parent,false)
        }
        return PhotoViewHolder(inflatedView)
    }

    override fun getItemViewType(position: Int) =
        if (filtering) {
            filteredPhotoList[position].type.ordinal
        } else {
            photoList[position].type.ordinal
        }

    fun switchCamera(camera: String) {
        filtering = true
        val newPhotos = photoList.filter { photo -> photo.type == RowType.PHOTO && photo.photo?.camera?.name.equals(camera) } as ArrayList<PhotoRow>
        DiffUtil.calculateDiff(PhotoRowDiffCallback(newPhotos, photoList), false).dispatchUpdatesTo(this)
        filteredPhotoList = newPhotos
    }


    private fun clearFilter() {
        filtering = false
        filteredPhotoList.clear()
    }

    fun updatePhotos(photos : ArrayList<PhotoRow>) {
        DiffUtil.calculateDiff(PhotoRowDiffCallback(photos, photoList), false).dispatchUpdatesTo(this)
        photoList = photos
        clearFilter()
    }

    fun removeRow(row : Int) {
        if (filtering) {
            filteredPhotoList.removeAt(row)
        } else {
            photoList.removeAt(row)
        }
        notifyItemRemoved(row)
    }

    private fun setAnimation(viewToAnimate: View) {
        if (viewToAnimate.animation == null) {
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.slide_in_left)
            viewToAnimate.animation = animation
        }
    }

    class PhotoRowDiffCallback(private val newRows : List<PhotoRow>, private val oldRows : List<PhotoRow>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldRow = oldRows[oldItemPosition]
            val newRow = newRows[newItemPosition]
            return oldRow.type == newRow.type
        }

        override fun getOldListSize(): Int = oldRows.size

        override fun getNewListSize(): Int = newRows.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldRow = oldRows[oldItemPosition]
            val newRow = newRows[newItemPosition]
            return oldRow == newRow
        }
    }
}