package com.sandeepsingh.imageupload.core.imageutil

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.sandeepsingh.imageupload.R
import com.sandeepsingh.imageupload.core.imageutil.ImageHelper.Companion.CENTER_CROP
import com.sandeepsingh.imageupload.core.imageutil.ImageHelper.Companion.CENTER_FIT
import com.sandeepsingh.imageupload.core.imageutil.ImageHelper.Companion.CENTER_INSIDE
import com.sandeepsingh.imageupload.core.imageutil.ImageHelper.Companion.CIRCLE_CROP

/**
 * Created by Sandeep on 10/24/18.
 */
class ImageProcessor : ImageLoader {
    override fun loadImage(context: Context, URL: String, fitType: Int, radius: Int, view: ImageView) {
        if (URL == null)
            throw IllegalStateException("Image URL string cannot be null.")
        else if (view == null)
            throw IllegalStateException("Image view cannot be null.")
        else if (context == null)
            throw IllegalStateException("Context cannot be null.")
        else if (fitType == CENTER_FIT) {
            Glide.with(context)
                .load(URL).thumbnail(MULTIPLER)
                .apply(RequestOptions.fitCenterTransform())
                .apply(
                    RequestOptions.bitmapTransform(RoundedCorners(radius)).placeholder(R.color.light_grey3).diskCacheStrategy(
                        DiskCacheStrategy.AUTOMATIC
                    ).priority(Priority.LOW)
                )
                .into(view)
        } else if (fitType == CENTER_CROP) {
            Glide.with(context)
                .load(URL).thumbnail(MULTIPLER)
                .apply(
                    RequestOptions.bitmapTransform(RoundedCorners(radius)).placeholder(R.color.light_grey3).diskCacheStrategy(
                        DiskCacheStrategy.AUTOMATIC
                    ).priority(Priority.LOW)
                )
                .apply(RequestOptions.centerCropTransform())
                .into(view)
        } else if (fitType == CENTER_INSIDE) {
            Glide.with(context)
                .load(URL).thumbnail(MULTIPLER)
                .apply(RequestOptions.centerInsideTransform())
                .apply(
                    RequestOptions.bitmapTransform(RoundedCorners(radius)).placeholder(R.color.light_grey3).diskCacheStrategy(
                        DiskCacheStrategy.AUTOMATIC
                    ).priority(Priority.LOW)
                )
                .into(view)
        } else if (fitType == CIRCLE_CROP) {

            val placeHolder = BitmapFactory.decodeResource(context.resources, R.color.light_grey3)
            val roundedPlaceHolder = RoundedBitmapDrawableFactory.create(context.resources, placeHolder)
            roundedPlaceHolder.isCircular = true

            Glide.with(context)
                .load(URL).thumbnail(MULTIPLER)
                .apply(RequestOptions.circleCropTransform())
                .apply(
                    RequestOptions.bitmapTransform(RoundedCorners(radius)).placeholder(roundedPlaceHolder).diskCacheStrategy(
                        DiskCacheStrategy.AUTOMATIC
                    ).priority(Priority.LOW)
                )
                .into(view)
        }
    }

    private val MULTIPLER = 1f

    override fun loadImage(context: Context, URL: String, fitType: Int, view: ImageView) {
        if (URL == null)
            throw IllegalStateException("Image URL string cannot be null.")
        else if (view == null)
            throw IllegalStateException("Image view cannot be null.")
        else if (context == null)
            throw IllegalStateException("Context cannot be null.")
        else if (fitType == CENTER_FIT) {
            Glide.with(context)
                .load(URL).thumbnail(MULTIPLER)
                .apply(
                    RequestOptions.fitCenterTransform().placeholder(R.color.light_grey3).diskCacheStrategy(
                        DiskCacheStrategy.AUTOMATIC
                    ).priority(
                        Priority.LOW
                    )
                )
                .into(view)
        } else if (fitType == CENTER_CROP) {
            Glide.with(context)
                .load(URL).thumbnail(MULTIPLER)
                .apply(
                    RequestOptions.centerCropTransform().placeholder(R.color.light_grey3).diskCacheStrategy(
                        DiskCacheStrategy.AUTOMATIC
                    ).priority(
                        Priority.LOW
                    )
                )
                .into(view)
        } else if (fitType == CENTER_INSIDE) {
            Glide.with(context)
                .load(URL).thumbnail(MULTIPLER)
                .apply(
                    RequestOptions.centerInsideTransform().placeholder(R.color.light_grey3).diskCacheStrategy(
                        DiskCacheStrategy.AUTOMATIC
                    ).priority(
                        Priority.LOW
                    )
                )
                .into(view)
        } else if (fitType == CIRCLE_CROP) {
            val placeHolder = BitmapFactory.decodeResource(context.resources, R.color.light_grey3)
            val roundedPlaceHolder = RoundedBitmapDrawableFactory.create(context.resources, placeHolder)
            roundedPlaceHolder.isCircular = true

            Glide.with(context)
                .load(URL).thumbnail(MULTIPLER)
                .apply(
                    RequestOptions.circleCropTransform().placeholder(roundedPlaceHolder).diskCacheStrategy(
                        DiskCacheStrategy.AUTOMATIC
                    ).priority(
                        Priority.LOW
                    )
                )
                .into(view)
        }
    }
}