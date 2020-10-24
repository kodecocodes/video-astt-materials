package com.raywenderlich.marsrovers.models

import com.raywenderlich.marsrovers.models.Photo

/**
 * Hold a list of photos
 * This has to be an object as the json return looks like:
 * {
 * "photos": [
 */
data class PhotoList(val photos: List<Photo>)