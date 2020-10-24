package com.raywenderlich.marsrovers.models

/**
 * Hold either a photo or a header string
 */
enum class RowType {
    PHOTO,
    HEADER
}

data class PhotoRow(var type: RowType, var photo: Photo?, var header: String?)