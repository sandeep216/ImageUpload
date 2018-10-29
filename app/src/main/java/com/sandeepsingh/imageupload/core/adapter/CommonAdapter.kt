package com.sandeepsingh.imageupload.feature.home.feed.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sandeepsingh.imageupload.R
import com.sandeepsingh.imageupload.core.Prefs
import com.sandeepsingh.imageupload.core.UserConstants
import com.sandeepsingh.imageupload.core.Utils
import com.sandeepsingh.imageupload.core.imageutil.CustomImageView
import com.sandeepsingh.imageupload.core.imageutil.ImageHelper
import com.sandeepsingh.imageupload.feature.comment.pojos.Comment
import com.sandeepsingh.imageupload.feature.comment.view.CommentActivity
import com.sandeepsingh.imageupload.feature.home.HomeActivity
import com.sandeepsingh.imageupload.feature.home.feed.pojos.Image
import java.io.Serializable

/**
 * Created by Sandeep on 10/24/18.
 */
class CommonAdapter(val context: Context, var dataArray: ArrayList<Serializable>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        if (context is HomeActivity) {
            val rootView = LayoutInflater.from(context).inflate(R.layout.layout_image_item, p0, false)
            return ImageViewHolder(rootView)
        } else {
            val rootView = LayoutInflater.from(context).inflate(R.layout.layout_comment, p0, false)
            return CommentViewHolder(rootView)
        }
    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (context is HomeActivity) {
            val imageData = dataArray[p1] as Image

            ImageHelper.load(
                context,
                imageData.imageUrl!!,
                ImageHelper.CENTER_FIT,
                (p0 as ImageViewHolder).imageView
            )


            if (imageData.imageUploadedByName != null) {
                p0.uploadedBy.text = "Posted By : " + imageData.imageUploadedByName
            }
            p0.uploadedAt.text = Utils.getDate(imageData.timeStamp!!)
            p0.ivComment.setOnClickListener {
                context.startActivity(CommentActivity.getIntent(context, imageData))
            }

            if (context.getUserData().getArrayOfInteraction().contains(imageData.imageId)) {
                p0.ivLike.setImageResource(R.drawable.ic_wishlisted)
                p0.ivLike.tag = true
            } else {
                p0.ivLike.setImageResource(R.drawable.ic_wishlist)
                p0.ivLike.tag = false
            }

            p0.ivLike.setOnClickListener {
                if (p0.ivLike.tag == true) {
                    p0.ivLike.setImageResource(R.drawable.ic_wishlist)
                    context.addUserInteraction(imageData.imageId!!, false)
                    notifyItemChanged(p1)
                } else {
                    p0.ivLike.setImageResource(R.drawable.ic_wishlisted)
                    context.addUserInteraction(imageData.imageId!!, true)
                    notifyItemChanged(p1)
                }
            }
        } else {
            val commentData = dataArray[p1] as Comment
            (p0 as CommentViewHolder).tvComment.text = commentData.comment
            if (Prefs.getString(context, UserConstants.KEY_USER_NAME, "").equals(commentData.commentBy)) {
                p0.tvSendBy.text = "You"
            } else {
                p0.tvSendBy.text = commentData.commentBy
            }
            p0.tvSendAt.text = Utils.getDate(commentData.commentAt!!)
        }
    }

    fun notify(imageDataArray: ArrayList<Serializable>) {
        dataArray = imageDataArray
        notifyDataSetChanged()
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView = view.findViewById(R.id.iv_image) as CustomImageView
        var ivLike = view.findViewById(R.id.iv_like) as ImageView
        var ivComment = view.findViewById<ImageView>(R.id.iv_comment)
        var uploadedBy = view.findViewById<TextView>(R.id.tv_uploaded_by)
        var uploadedAt = view.findViewById<TextView>(R.id.tv_uploaded_at)
    }

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvSendBy = view.findViewById<TextView>(R.id.tv_send_by)
        var tvSendAt = view.findViewById<TextView>(R.id.tv_send_at)
        var tvComment = view.findViewById<TextView>(R.id.tv_comment)
    }
}