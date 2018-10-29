package com.sandeepsingh.imageupload.feature.login

import android.app.Activity
import android.content.Intent
import com.sandeepsingh.imageupload.core.LoginConstants

/**
 * Created by Sandeep on 10/22/18.
 */
class SocialManagement(socialType: Int, private var activity: Activity, loginCallback: LoginCallback) {
    private val TAG = SocialManagement::class.java.simpleName

    /*
     * This variable is from FEx series. It will identify which button was pressed and accordingly actions were done
     */
    var mSocialType: Int = socialType
    private var mCallback: LoginCallback = loginCallback
    private lateinit var facebookLogin: FacebookLogin
    private var googleLogin: GoogleLogin? = null

    fun login() {
        when (mSocialType) {
            LoginConstants.PROVIDER_FACEBOOK -> {
                facebookLogin = FacebookLogin(activity, mCallback)
                facebookLogin.login()
            }

            LoginConstants.PROVIDER_GOOGLE -> {
                googleLogin = GoogleLogin(activity, mCallback)
                googleLogin!!.login()
            }
        }
    }

    fun onSocialResult(requestCode: Int, resultCode: Int, data: Intent) {

        when (mSocialType) {
            LoginConstants.PROVIDER_FACEBOOK -> if (null != facebookLogin)
                facebookLogin.onSocialResult(requestCode, resultCode, data)

            LoginConstants.PROVIDER_GOOGLE -> if (googleLogin != null) {
                googleLogin!!.onSocialResult(requestCode, resultCode, data)
            }
        }
    }
}