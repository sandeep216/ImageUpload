package com.sandeepsingh.imageupload.feature.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.widget.Toolbar
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sandeepsingh.imageupload.R
import com.sandeepsingh.imageupload.core.*
import com.sandeepsingh.imageupload.feature.home.feed.pojos.Image
import com.sandeepsingh.imageupload.feature.home.feed.view.FeedFragment
import com.sandeepsingh.imageupload.feature.home.profile.view.ProfileFragment
import com.sandeepsingh.imageupload.feature.login.pojos.UserInfo
import com.sandeepsingh.imageupload.feature.notification.UserToken
import kotlinx.android.synthetic.main.activity_home.*

/**
 * Created by Sandeep on 10/23/18.
 */
class HomeActivity : BaseActivity() {

    companion object {
        val FEED = 0
        //val ADD = 1
        val PROFILE = 1
        val KEY_USER_INFO = "user_info"

        fun getIntent(activity: Activity, userInfo: UserInfo): Intent {
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra(KEY_USER_INFO, userInfo)
            return intent
        }
    }

    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout
    lateinit var pagerAdapter: PagerAdapter
    var userInfo = UserInfo()

    var fragmentsList: ArrayList<Fragment> = ArrayList()
    lateinit var profileFramgent: ProfileFragment
    lateinit var feedFragment: FeedFragment
    lateinit var mStorage: StorageReference
    lateinit var imageDatabaseReference: DatabaseReference
    lateinit var userDatabaseReference: DatabaseReference
    lateinit var loaderView : View
    lateinit var toolbar: android.support.v7.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        retrieveBundle()
        initComponents()
        initFragments()
        initViewPager()
        initTabs()
        initViews()
    }

    fun retrieveBundle() {
        userInfo = intent.extras.getSerializable(KEY_USER_INFO) as UserInfo
    }

    fun initComponents() {
        mStorage = FirebaseStorage.getInstance().reference
        imageDatabaseReference = FirebaseDatabase.getInstance().getReference("images")
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        loaderView = findViewById(R.id.loader)
        setupToolbar()
    }

    fun setupToolbar(){
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (null != supportActionBar) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
    }

    fun initFragments() {
        val bundle = Bundle()
        bundle.putSerializable(KEY_USER_INFO, userInfo)
        profileFramgent = ProfileFragment()
        profileFramgent.arguments = bundle
        feedFragment = FeedFragment()
        feedFragment.databaseReference(imageDatabaseReference)
        fragmentsList.add(feedFragment)
        fragmentsList.add(profileFramgent)
    }

    fun initViewPager() {
        viewPager = findViewById(R.id.view_pager)
        pagerAdapter = PagerAdapter(supportFragmentManager, fragmentsList)
        viewPager.offscreenPageLimit = 1
        viewPager.adapter = pagerAdapter
        viewPager.setCurrentItem(FEED, true)
    }

    fun initTabs() {
        tabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(FEED)!!.icon = ContextCompat.getDrawable(this@HomeActivity, R.drawable.footer_tab_feed)
      //  tabLayout.getTabAt(ADD)!!.icon = ContextCompat.getDrawable(this@HomeActivity, R.drawable.ic_add_photo)
        tabLayout.getTabAt(PROFILE)!!.icon = ContextCompat.getDrawable(this@HomeActivity, R.drawable.footer_tab_profile)
        /*tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == tabLayout.getTabAt(ADD)){
                    feedFragment.askForPermission()
                }
            }

        })*/
    }

    fun initViews(){
        fab.setOnClickListener {
            if (feedFragment != null) {
                feedFragment.askForPermission()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            FeedFragment.REQUEST_CODE_CAMERA -> {
                feedFragment.openIntentLauncher()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FeedFragment.GALLERY_INTENT) {
                if (data!!.data != null) {
                    val uri = data.data!!
                    addImageToDataStorage(uri)
                }
            }
        }
    }

    fun addImageToDataStorage(uri: Uri) {
        showProgressDialog(loaderView,true)
        val filePath = mStorage.child("Photos").child(uri.lastPathSegment!!)
        filePath.putFile(uri).addOnSuccessListener {

            fetchLinkOfImage(uri)
        }.addOnFailureListener {
            showProgressDialog(loaderView,false)
            Utils.showShortToast(this, "Upload Failure")
        }
    }

    fun fetchLinkOfImage(uri: Uri){
        mStorage.child("Photos").child(uri.lastPathSegment!!).downloadUrl.addOnSuccessListener {
            addImageToDataBase(it.toString())
            Log.d("Login", uri.toString())
            showProgressDialog(loaderView,false)
        }.addOnFailureListener {
            Utils.showShortToast(this, "No uri!")
            showProgressDialog(loaderView,false)
        }
    }

    fun addImageToDataBase(imageUrl: String) {
        val imageId = imageDatabaseReference.push().key
        if (imageId != null) {
            val image = Image(
                imageId,
                imageUrl,
                Prefs.getString(this, UserConstants.KEY_USER_ID, "")!!,
                Prefs.getString(this,UserConstants.KEY_USER_NAME,"")!!
            )
            imageDatabaseReference.child(imageId).setValue(image).addOnSuccessListener {
                Utils.showShortToast(this, "Your post have been successfully uploaded!")
            }.addOnFailureListener {
                Utils.showShortToast(this, it.message!!)
            }

        } else {
            Utils.showShortToast(this, "No id found")
        }

    }

    fun addUserInteraction(imageId: String, isLiked: Boolean) {
        val arrayInteraction = userInfo.getArrayOfInteraction()
        if (isLiked) {
            arrayInteraction.add(imageId)
        } else {
            arrayInteraction.remove(imageId)
        }
        userInfo.setArrayOfInteractions(arrayInteraction)

        val childDatabaseRef = userDatabaseReference.child(userInfo.getId()!!)
        childDatabaseRef.setValue(userInfo)
    }

    fun getUserData() : UserInfo{
        return userInfo
    }
}