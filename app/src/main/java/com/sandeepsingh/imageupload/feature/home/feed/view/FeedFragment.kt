package com.sandeepsingh.imageupload.feature.home.feed.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DatabaseReference
import com.sandeepsingh.imageupload.R
import com.sandeepsingh.imageupload.feature.home.feed.IFeed
import com.sandeepsingh.imageupload.feature.home.feed.adapter.CommonAdapter
import com.sandeepsingh.imageupload.feature.home.feed.model.FeedModel
import com.sandeepsingh.imageupload.feature.home.feed.pojos.Image
import com.sandeepsingh.imageupload.feature.home.feed.presenter.FeedPresenter
import kotlinx.android.synthetic.main.fragment_feed.*
import java.io.Serializable

/**
 * Created by Sandeep on 10/23/18.
 */
class FeedFragment : Fragment(), IFeed.PresenterToView {
    companion object {
        const val GALLERY_INTENT = 1
        const val REQUEST_CODE_CAMERA = 2
    }

  //  lateinit var fabUpload: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    var imageAdapter : CommonAdapter ?= null
    var presenter : FeedPresenter ?= null
    var mDatabaseReference : DatabaseReference ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        setupMvp()
        initComponents(view)
        presenter!!.loadData(mDatabaseReference!!)
        return view
    }

    fun initComponents(view: View){
      //  fabUpload = view.findViewById(R.id.fab)
        recyclerView = view.findViewById(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(activity!!)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        imageAdapter = CommonAdapter(activity!!, arrayListOf())
        recyclerView.adapter = imageAdapter
    }


    fun setupMvp(){
        presenter = FeedPresenter(this)
        val model = FeedModel(presenter!!)
        presenter!!.setModel(model)
    }

    fun openIntentLauncher(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity!!.startActivityForResult(intent, GALLERY_INTENT)
    }

    fun askForPermission() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity!!,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_CAMERA)
        } else {
            openIntentLauncher()
        }
    }

    fun databaseReference(databaseReference: DatabaseReference){
        mDatabaseReference = databaseReference
    }

    override fun notifyDataChanged(imageDataArray : ArrayList<Serializable>){
        if (imageDataArray.size > 0) {
            imageAdapter!!.notify(imageDataArray)
            recyclerView.smoothScrollToPosition(imageDataArray.size - 1)
        }
    }
}