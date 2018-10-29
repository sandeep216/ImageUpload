package com.sandeepsingh.imageupload.feature.home.feed.model

import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.sandeepsingh.imageupload.feature.home.feed.IFeed
import com.sandeepsingh.imageupload.feature.home.feed.pojos.Image
import com.sandeepsingh.imageupload.feature.home.feed.presenter.FeedPresenter
import java.io.Serializable

/**
 * Created by Sandeep on 10/24/18.
 */
class FeedModel(val presenter : FeedPresenter) : IFeed.PresenterToModel {

    var imageList = ArrayList<Serializable>()

    override fun loadData(databaseReference: DatabaseReference) {
        val imageQuery = databaseReference.orderByChild("timeStamp")
        imageQuery.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                imageList.clear()
                for (postSnapshot in p0.children) {
                    imageList.add(postSnapshot.getValue(Image::class.java)!!)
                }
                presenter.notifyDataChanged(imageList)
            }

        })
    }
}