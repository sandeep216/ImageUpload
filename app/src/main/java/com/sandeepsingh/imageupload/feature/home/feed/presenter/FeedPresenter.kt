package com.sandeepsingh.imageupload.feature.home.feed.presenter

import com.google.firebase.database.DatabaseReference
import com.sandeepsingh.imageupload.core.BasePresenter
import com.sandeepsingh.imageupload.feature.home.feed.IFeed
import com.sandeepsingh.imageupload.feature.home.feed.model.FeedModel
import com.sandeepsingh.imageupload.feature.home.feed.pojos.Image
import java.io.Serializable

/**
 * Created by Sandeep on 10/24/18.
 */
class FeedPresenter(view : IFeed.PresenterToView) : BasePresenter<IFeed.PresenterToView>(view),IFeed.ModelToPresenter,IFeed.ViewToPresenter  {

    private var model : FeedModel ?=null

    fun setModel(model : FeedModel){
        this.model = model
    }
    override fun loadData(databaseReference: DatabaseReference) {
        model!!.loadData(databaseReference)
    }

    override fun notifyDataChanged(imageDataArray: ArrayList<Serializable>) {
        getView()!!.notifyDataChanged(imageDataArray)
    }
}