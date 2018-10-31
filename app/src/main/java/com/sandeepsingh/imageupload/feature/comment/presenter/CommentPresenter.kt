package com.sandeepsingh.imageupload.feature.comment.presenter

import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.sandeepsingh.imageupload.core.BasePresenter
import com.sandeepsingh.imageupload.feature.comment.IComment
import com.sandeepsingh.imageupload.feature.comment.model.CommentModel
import com.sandeepsingh.imageupload.feature.comment.pojos.Comment
import com.sandeepsingh.imageupload.feature.home.feed.pojos.Image
import java.io.Serializable

/**
 * Created by Sandeep on 10/24/18.
 */
class CommentPresenter(view : IComment.PresenterToView) : BasePresenter<IComment.PresenterToView>(view),IComment.ViewToPresenter,IComment.ModelToPresenter {
    override fun isDatabaseError(b: Boolean) {
        getView()!!.isDatabaseError(b)
    }

    override fun getAppContext(): Context {
        return getView()!!.getAppContext()
    }

    override fun getActivityContext(): Context {
        return getView()!!.getActivityContext()
    }

    var commentModel : CommentModel ?= null

    fun setModel(model : CommentModel){
        this.commentModel = model
    }

    override fun loadComments(databaseReference: DatabaseReference,imageId : String) {
        commentModel!!.loadComments(databaseReference, imageId)
    }

    override fun storeComment(databaseReference: DatabaseReference, comment: Comment) {
        commentModel!!.storeComment(databaseReference, comment)
    }

    override fun notifyDataChanged(commentArray: ArrayList<Serializable>) {
        getView()!!.notifyDataChanged(commentArray)
    }

    override fun getImage() : Image {
        return getView()!!.getImage()
    }

    override fun sendNotification(userToken: String) {
        getView()!!.sendNotification(userToken)
    }

}