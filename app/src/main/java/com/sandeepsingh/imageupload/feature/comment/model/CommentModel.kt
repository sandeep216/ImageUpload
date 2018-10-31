package com.sandeepsingh.imageupload.feature.comment.model

import android.util.Log
import com.google.firebase.database.*
import com.google.gson.JsonElement
import com.sandeepsingh.imageupload.core.*
import com.sandeepsingh.imageupload.feature.comment.IComment
import com.sandeepsingh.imageupload.feature.comment.pojos.Comment
import com.sandeepsingh.imageupload.feature.comment.presenter.CommentPresenter
import com.sandeepsingh.imageupload.feature.home.feed.pojos.Image
import com.sandeepsingh.imageupload.feature.notification.UserToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.lang.Exception

/**
 * Created by Sandeep on 10/24/18.
 */
class CommentModel(val presenter: IComment.ModelToPresenter) : IComment.PresenterToModel {

    var commentArray = ArrayList<Serializable>()
    var imageId : String ?= null

    override fun loadComments(databaseReference: DatabaseReference, imageId: String) {
        fetchComments()
        val query = databaseReference.orderByChild("imageId").startAt(imageId).endAt(imageId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                presenter.isDatabaseError(true)
            }

            override fun onDataChange(p0: DataSnapshot) {
                presenter.isDatabaseError(false)
                commentArray.clear()
                for (postSnapshot in p0.children) {
                    commentArray.add(postSnapshot.getValue(Comment::class.java)!!)
                }
                presenter.notifyDataChanged(commentArray)
            }

        })
    }

    override fun storeComment(databaseReference: DatabaseReference, comment: Comment) {
        databaseReference.child(comment.commentId!!).setValue(comment).addOnSuccessListener {
            Utils.showShortToast(presenter.getActivityContext(), "Saved, ")
            sendNotification(presenter.getImage())
        }.addOnFailureListener {
            Utils.showShortToast(presenter.getActivityContext(), it.message!!)
            Log.d("Login", it.message)
        }
    }

    fun sendNotification(imageData: Image) {
        val query =
            FirebaseDatabase.getInstance().getReference("token").orderByChild("userId").startAt(imageData.userId)
                .endAt(imageData.userId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("Login", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val firebaaseToken = p0.children.iterator().next().getValue(UserToken::class.java)!!.firebaseToken
                    if (firebaaseToken != null) {
                        presenter.sendNotification(firebaaseToken)
                    }
                } catch (e: Exception) {
                    Log.d("Login",e.toString())
                }

            }

        })
    }

    fun fetchComments(){
        if (!imageId.isNullOrEmpty()) {
            try {
                val iRetrofitCalls =
                    Utils.getRetrofitInstance(NotificationContants.FIREBASE_URL).create(IRetrofitCalls::class.java)
                val call = iRetrofitCalls.getComments("imageId", "")
                val callback = object : Callback<JsonElement> {
                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        presenter.isDatabaseError(true)
                    }

                    override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                        presenter.isDatabaseError(false)
                    }

                }
                call.enqueue(callback)
            } catch (e: Exception) {
                presenter.isDatabaseError(true)
            }
        } else {
            presenter.isDatabaseError(true)
        }
    }

}