package com.sandeepsingh.imageupload.feature.comment.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sandeepsingh.imageupload.R
import com.sandeepsingh.imageupload.core.BaseActivity
import com.sandeepsingh.imageupload.core.LoginConstants
import com.sandeepsingh.imageupload.core.Prefs
import com.sandeepsingh.imageupload.core.UserConstants
import com.sandeepsingh.imageupload.feature.comment.IComment
import com.sandeepsingh.imageupload.feature.comment.model.CommentModel
import com.sandeepsingh.imageupload.feature.comment.pojos.Comment
import com.sandeepsingh.imageupload.feature.comment.presenter.CommentPresenter
import com.sandeepsingh.imageupload.feature.home.feed.adapter.CommonAdapter
import com.sandeepsingh.imageupload.feature.home.feed.pojos.Image
import kotlinx.android.synthetic.main.activity_comment.*
import java.io.Serializable

/**
 * Created by Sandeep on 10/24/18.
 */
class CommentActivity : BaseActivity(), IComment.PresenterToView {

    companion object {
        val IMAGE_DATA = "image_data"

        fun getIntent(activity: Activity,imageData: Image) : Intent{
            val intent = Intent(activity,CommentActivity::class.java)
            intent.putExtra(IMAGE_DATA,imageData)
            return intent
        }
    }

    var databaseReference : DatabaseReference?=null
    var imageData : Image ?= null
    var commonAdapter : CommonAdapter ?=null
    var presenter : CommentPresenter ?= null
    lateinit var loaderView : View
    lateinit var toolbar: android.support.v7.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        setupMvp()
        retrieveBundle()
        initComponents()
        initView()
        showProgressDialog(loaderView,true)
        presenter!!.loadComments(databaseReference!!,imageData!!.imageId!!)
    }

    fun setupMvp(){
        presenter = CommentPresenter(this)
        val model = CommentModel(presenter!!)
        presenter!!.setModel(model)
    }

    fun retrieveBundle(){
        imageData = intent.extras!!.getSerializable(IMAGE_DATA) as Image
    }

    fun initComponents(){
        databaseReference = FirebaseDatabase.getInstance().getReference("comments")
        loaderView = findViewById(R.id.loader)
        setupToolbar()
    }

    fun setupToolbar(){
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val image_back = toolbar.findViewById<ImageView>(R.id.iv_back)
        image_back.visibility = View.VISIBLE

        if (null != supportActionBar) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        image_back.setOnClickListener {
            onBackPressed()
        }
    }

    fun initView(){
        recycler_view.layoutManager = LinearLayoutManager(this)
        iv_send_comment.setOnClickListener {
            if (!TextUtils.isEmpty(et_comment.text)) {
                val comment = Comment()
                comment.comment = et_comment.text.toString()
                comment.commentAt = System.currentTimeMillis()
                comment.commentById = Prefs.getString(this,UserConstants.KEY_USER_ID,"")
                comment.commentBy = Prefs.getString(this,UserConstants.KEY_USER_NAME,"")
                comment.imageId = imageData!!.imageId
                val id = databaseReference!!.push().key
                comment.commentId = id
                et_comment.text.clear()
                presenter!!.storeComment(databaseReference!!,comment)
            }
        }

        et_comment.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank() && !s.isNullOrEmpty()){
                    iv_send_comment.setTextColor(Color.parseColor("#000000"))
                } else {
                    iv_send_comment.setTextColor(ContextCompat.getColor(this@CommentActivity,R.color.light_grey3))
                }
            }

        })
    }

    override fun notifyDataChanged(commentArray: ArrayList<Serializable>) {
        showProgressDialog(loaderView,false)
        commonAdapter = CommonAdapter(this,commentArray)
        recycler_view.adapter = commonAdapter
        commonAdapter!!.notifyDataSetChanged()
    }

    override fun getAppContext(): Context {
        return applicationContext
    }

    override fun getActivityContext(): Context {
        return this
    }

    override fun getImage() : Image{
        return imageData!!
    }

    override fun sendNotification(userToken: String) {
        val userName = Prefs.getString(this,UserConstants.KEY_USER_NAME,"")
        sendPushNotification(userToken,"$userName have commented on your post")
    }
}