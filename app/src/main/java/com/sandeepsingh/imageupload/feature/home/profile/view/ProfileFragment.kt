package com.sandeepsingh.imageupload.feature.home.profile.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.sandeepsingh.imageupload.R
import com.sandeepsingh.imageupload.core.imageutil.ImageHelper
import com.sandeepsingh.imageupload.feature.home.HomeActivity
import com.sandeepsingh.imageupload.feature.login.pojos.UserInfo
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * Created by Sandeep on 10/23/18.
 */
class ProfileFragment : Fragment() {

    lateinit var tvName : TextView
    lateinit var tvEmail : TextView
    lateinit var ivUserImage : ImageView

    var userInfo = UserInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrieveBundle()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        tvName = rootView.findViewById(R.id.tv_name)
        tvEmail = rootView.findViewById(R.id.tv_email)
        ivUserImage = rootView.findViewById(R.id.iv_user_image)
        initViews()
        return rootView
    }

    fun retrieveBundle(){
        userInfo = arguments!!.getSerializable(HomeActivity.KEY_USER_INFO) as UserInfo
    }

    fun initViews(){
        tvName.text = "Hi, " + userInfo.getFirstName()
        tvEmail.text = userInfo.getEmail()
        ImageHelper.load(activity!!,userInfo.getProfilePic()!!,ImageHelper.CENTER_FIT,20,ivUserImage)
    }
}