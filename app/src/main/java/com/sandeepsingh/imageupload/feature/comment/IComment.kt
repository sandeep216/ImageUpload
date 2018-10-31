package com.sandeepsingh.imageupload.feature.comment

import com.google.firebase.database.DatabaseReference
import com.sandeepsingh.imageupload.core.IContext
import com.sandeepsingh.imageupload.feature.comment.pojos.Comment
import com.sandeepsingh.imageupload.feature.home.feed.pojos.Image
import java.io.Serializable

/**
 * Created by Sandeep on 10/24/18.
 */
interface IComment {

    interface ViewToPresenter{
        fun loadComments(databaseReference: DatabaseReference,imageId : String)
        fun storeComment(databaseReference: DatabaseReference,comment: Comment)
    }

    interface PresenterToModel{
        fun loadComments(databaseReference: DatabaseReference,imageId : String)
        fun storeComment(databaseReference: DatabaseReference,comment: Comment)
    }

    interface ModelToPresenter : IContext{
        fun notifyDataChanged(commentArray : ArrayList<Serializable>)
        fun getImage(): Image
        fun sendNotification(userToken: String)
        fun isDatabaseError(b: Boolean)
    }

    interface PresenterToView : IContext{
        fun notifyDataChanged(commentArray : ArrayList<Serializable>)
        fun getImage(): Image
        fun sendNotification(userToken: String)
        fun isDatabaseError(b: Boolean)
    }
}