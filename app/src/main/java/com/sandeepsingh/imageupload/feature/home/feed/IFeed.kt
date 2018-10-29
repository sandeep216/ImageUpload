package com.sandeepsingh.imageupload.feature.home.feed

import com.google.firebase.database.DatabaseReference
import com.sandeepsingh.imageupload.feature.home.feed.pojos.Image
import java.io.Serializable

/**
 * Created by Sandeep on 10/24/18.
 */
interface IFeed {

    interface ViewToPresenter{
        fun loadData(databaseReference: DatabaseReference)
    }

    interface PresenterToModel{
        fun loadData(databaseReference: DatabaseReference)
    }

    interface ModelToPresenter{
        fun notifyDataChanged(imageDataArray : ArrayList<Serializable>)
    }

    interface PresenterToView{
        fun notifyDataChanged(imageDataArray : ArrayList<Serializable>)
    }
}