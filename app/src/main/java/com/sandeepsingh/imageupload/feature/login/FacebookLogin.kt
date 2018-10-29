package com.sandeepsingh.imageupload.feature.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.sandeepsingh.imageupload.core.LoginConstants
import com.sandeepsingh.imageupload.feature.login.pojos.UserInfo
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Sandeep on 10/22/18.
 */
class FacebookLogin(private var activity: Activity, private var loginCallback: LoginCallback) {
    private val TAG = FacebookLogin::class.java.simpleName

    private var mCallback: CallbackManager? = null

    private var mUserInfo: UserInfo? = null
//    private Profile mProfile;

    fun getUserInfo(): UserInfo {
        if (null == mUserInfo)
            throw NullPointerException("No user info found")
        return mUserInfo!!
    }

    fun login() {
        try {
            //  FacebookSdk.sdkInitialize(activity);
            mCallback = CallbackManager.Factory.create()
            val loginManager = LoginManager.getInstance()
//            loginManager.logOut()
            loginManager.logInWithReadPermissions(
                activity, Arrays.asList(
                    "email",
                    "public_profile"
                )
            )

            loginManager.registerCallback(mCallback!!, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    val graphRequest = GraphRequest.newMeRequest(loginResult.accessToken) { jsonObject, graphResponse ->
                        if (null != jsonObject) {
                            try {
                                val fullName = jsonObject.getString("name")
                                var facebookId = ""
                                if (jsonObject.has("id")) {
                                    facebookId = jsonObject.getString("id")
                                }
                                var profileUrl = ""
                                if (facebookId != null || TextUtils.isEmpty(facebookId)) {
                                    profileUrl = "http://graph.facebook.com/" + facebookId + "/picture?type=square"
                                }
                                var email: String? = null
                                if (jsonObject.has("email"))
                                    email = jsonObject.getString("email")

                                var firstName: String? = null
                                var lastName: String? = null
                                if (fullName.contains(" ")) {
                                    firstName = fullName.substring(0, fullName.indexOf(" ")).trim { it <= ' ' }
                                    lastName = fullName.substring(fullName.indexOf(" ")).trim { it <= ' ' }
                                }

                                val token = AccessToken.getCurrentAccessToken()
                                mUserInfo = UserInfo(
                                    "",
                                    email!!,
                                    firstName!!,
                                    lastName!!,
                                    "",
                                    LoginConstants.P_FACEBOOK,
                                    if (token == null) "" else token.token,
                                    ArrayList<String>(),
                                    profileUrl
                                )

                                loginCallback.onCompleteLogin(
                                    false,
                                    activity.javaClass.simpleName,
                                    "Success",
                                    mUserInfo!!
                                )
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                loginCallback.onCompleteLogin(
                                    true,
                                    activity.javaClass.simpleName,
                                    e.message!!,
                                    UserInfo(LoginConstants.P_FACEBOOK)
                                )
                            }

                        } else {
                            loginCallback.onCompleteLogin(
                                true,
                                activity.javaClass.simpleName,
                                "Error while login through facebook",
                                UserInfo(LoginConstants.P_FACEBOOK)
                            )
                        }
                    }

                    val parameters = Bundle()
                    parameters.putString("fields", "id, name, email, gender, birthday")
                    graphRequest.parameters = parameters
                    graphRequest.executeAsync()
                }

                override fun onCancel() {
                    loginCallback.onCompleteLogin(true, activity.javaClass.simpleName, null, null)
                }

                override fun onError(e: FacebookException) {
                    loginCallback.onCompleteLogin(
                        true,
                        activity.javaClass.simpleName,
                        e.message!!,
                        UserInfo(LoginConstants.P_FACEBOOK)
                    )
                }
            })
        } catch (e: Exception) {
            Log.d("Facebook", "Executive" + e.toString())
        }
    }


    fun onSocialResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (Activity.RESULT_CANCELED == resultCode)
            loginCallback.onCompleteLogin(
                true,
                activity.javaClass.simpleName,
                null,
                UserInfo(LoginConstants.P_FACEBOOK)
            )
        mCallback!!.onActivityResult(requestCode, resultCode, data)
    }
}