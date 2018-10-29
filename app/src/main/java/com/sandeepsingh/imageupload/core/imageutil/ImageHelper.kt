package com.sandeepsingh.imageupload.core.imageutil

import android.app.Activity
import android.content.Context
import android.widget.ImageView

/**
 * Created by Sandeep on 10/24/18.
 */
class ImageHelper {

    companion object {
        val CENTER_FIT = 0
        val CENTER_CROP = 1
        val CENTER_INSIDE = 2
        val CIRCLE_CROP = 3


        fun load(context: Context, URL: String, fitType: Int, view: ImageView) {
            if (isValidContextForGlide(context)) ImageProcessor().loadImage(context, URL, fitType, view)
        }

        fun load(context: Context, URL: String, fitType: Int, radius: Int, view: ImageView) {
            if (isValidContextForGlide(context)) ImageProcessor().loadImage(context, URL, fitType, radius, view)
        }

        private fun isValidContextForGlide(context: Context?): Boolean {
            if (context == null) {
                return false
            }
            if (context is Activity) {
                val activity = context as Activity?
                return !activity!!.isDestroyed && !activity.isFinishing
            }
            return true
        }

    }
}