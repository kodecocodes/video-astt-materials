/*
 * Copyright (c) 2017 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */


package com.raywenderlich.marsrovers

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.raywenderlich.marsrovers.models.Photo
import com.raywenderlich.marsrovers.models.PhotoList
import com.raywenderlich.marsrovers.models.PhotoRow
import com.raywenderlich.marsrovers.models.RowType
import com.raywenderlich.marsrovers.recyclerview.PhotoAdapter
import com.raywenderlich.marsrovers.services.NASAPhotos
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
  var currentRover = "curiosity"
  var currentRoverPosition = 0
  var currentCameraPosition = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setupSpinners()
    // Add Line separator
    recycler_view.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(this@MainActivity, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
    recycler_view.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@MainActivity)
    loadPhotos()
  }

  private fun setupSpinners() {
    setupRoverSpinner()
    setupCameraSpinner(resources.getStringArray(R.array.camera_values))
  }

  private val cameraAdapter = ArrayAdapter.createFromResource(this, R.array.camera_names, android.R.layout.simple_spinner_item)

  private fun setupCameraSpinner(cameraStrings: Array<String>) {
    // Camera spinner
    cameraAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    cameras.adapter = cameraAdapter
    cameras.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onNothingSelected(parent: AdapterView<*>) {
      }

      override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        if (recycler_view.adapter != null && currentCameraPosition != position) {
          (recycler_view.adapter as PhotoAdapter).switchCamera(cameraStrings[position])
        }
        currentCameraPosition = position
      }
    }
  }

  private fun setupRoverSpinner() {
    // Setup the spinners for selecting different rovers and cameras
    val roverStrings = resources.getStringArray(R.array.rovers)
    val adapter = ArrayAdapter.createFromResource(this, R.array.rovers, android.R.layout.simple_spinner_item)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    rovers.adapter = adapter
    rovers.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onNothingSelected(parent: AdapterView<*>) {
      }

      override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        if (currentRoverPosition != position) {
          currentRover = roverStrings[position].toLowerCase()
          loadPhotos()
        }
        currentRoverPosition = position
      }
    }
  }

  fun loadPhotos() {
    NASAPhotos.getPhotos(currentRover).enqueue(object : Callback<PhotoList> {
      override fun onFailure(call: Call<PhotoList>?, t: Throwable?) {
        Snackbar.make(recycler_view, R.string.api_error, Snackbar.LENGTH_LONG)
        Log.e(TAG, "Problems getting Photos with error: $t.msg")
      }

      override fun onResponse(call: Call<PhotoList>?, response: Response<PhotoList>?) {
        response?.let { photoResponse ->
          if (photoResponse.isSuccessful) {
            val photoList = photoResponse.body()!!
            Log.d(TAG, "Received ${photoList.photos.size} photos")
            if (recycler_view.adapter == null) {
              val adapter = PhotoAdapter(sortPhotos(photoList))
              recycler_view.adapter = adapter
              val touchHandler = ItemTouchHelper(SwipeHandler(adapter, 0, (ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)))
              touchHandler.attachToRecyclerView(recycler_view)
            } else {
              (recycler_view.adapter as PhotoAdapter).updatePhotos(sortPhotos(photoList))
            }
          }
        }
      }
    })
  }

  fun sortPhotos(photoList: PhotoList) : ArrayList<PhotoRow> {
    val map = HashMap<String, ArrayList<Photo>>()
    for (photo in photoList.photos) {
      var photos = map[photo.camera.full_name]
      if (photos == null) {
        photos = ArrayList<Photo>()
        map[photo.camera.full_name] = photos
      }
      photos.add(photo)
    }
    val newPhotos = ArrayList<PhotoRow>()
    for ((key, value) in map) {
      newPhotos.add(PhotoRow(RowType.HEADER, null, key))
      for (photo in value) {
        newPhotos.add(PhotoRow(RowType.PHOTO, photo, null))
      }
    }
    return newPhotos
  }

  /**
   * SwipeHandler. Romoves the row  when swiping left or right
   */
  class SwipeHandler(val adapter: PhotoAdapter, dragDirs : Int, swipeDirs : Int) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
      return false
    }

    override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {
      adapter.removeRow(viewHolder.adapterPosition)
    }

  }
  companion object {
    const val TAG = "MarsRover"
  }
}
