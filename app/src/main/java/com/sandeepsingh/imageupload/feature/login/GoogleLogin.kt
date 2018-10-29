package com.sandeepsingh.imageupload.feature.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.sandeepsingh.imageupload.core.LoginConstants
import com.sandeepsingh.imageupload.feature.login.pojos.UserInfo

/**
 * Created by Sandeep on 10/22/18.
 */
class GoogleLogin(private var activity: Activity, private var callback: LoginCallback) {
    private var userInfo: UserInfo? = null

    private val TAG = "Google Sign"

    private val RC_SIGN_IN = 359

    fun login() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(LoginConstants.GOOGLE_WEBCLIENT_ID)
                .requestEmail()
                .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        googleSignInClient.signOut()

        val signInIntent = googleSignInClient.getSignInIntent()
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun onSocialResult(requestCode: Int, resultCode: Int, data: Intent) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            userInfo = UserInfo(account!!.id!!, account.email!!, account.givenName!!, account.familyName!!, "", LoginConstants.P_GOOGLE, account.idToken,ArrayList<String>(),account.photoUrl.toString())
            callback.onCompleteLogin(false, activity.javaClass.simpleName, "Success", userInfo)

        } catch (e: ApiException) {

            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            if (e != null && e.statusCode == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {

            } else {
                callback.onCompleteLogin(true, activity.javaClass.simpleName, "Error logging through Google", UserInfo(LoginConstants.P_GOOGLE))
            }

        }

    }
}