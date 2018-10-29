package com.sandeepsingh.imageupload.core.imageutil

import android.content.Context
import android.widget.ImageView

/**
 * Created by Sandeep on 10/24/18.
 */
interface ImageLoader {
    fun loadImage(context: Context, URL: String, fitType: Int, view: ImageView)
    fun loadImage(context: Context, URL: String, fitType: Int, radius: Int, view: ImageView)
}