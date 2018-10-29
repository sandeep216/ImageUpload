package com.sandeepsingh.imageupload.feature.login.model

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FacebookAuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.sandeepsingh.imageupload.core.*
import com.sandeepsingh.imageupload.feature.home.HomeActivity
import com.sandeepsingh.imageupload.feature.login.ILogin
import com.sandeepsingh.imageupload.feature.login.pojos.UserInfo
import com.sandeepsingh.imageupload.feature.login.presenter.LoginPresenter
import com.sandeepsingh.imageupload.feature.login.view.LoginActivity
import com.sandeepsingh.imageupload.feature.notification.UserToken

/**
 * Created by Sandeep on 10/22/18.
 */
class LoginModel(var mPresenter : LoginPresenter, var mDatabaseInstance : DatabaseReference) : ILogin.PresenterToModel {

    override fun onLogin(userInfo: UserInfo) {
        try {
            if (userInfo != null) {
                userInfo.setArrayOfInteractions(arrayListOf())
                if (userInfo.getProvider().equals(LoginConstants.P_GOOGLE)) {
                    googleAuthUser(userInfo)
                } else {
                    facebookAuthUser(userInfo)
                }
                Utils.showShortToast(mPresenter.getActivityContext(),"Success")
            } else {
                Utils.showShortToast(mPresenter.getActivityContext(),"No id")
            }
        } catch (e:Exception){
            Utils.showShortToast(mPresenter.getActivityContext(),"Something went wrong!")
            mPresenter.showProgress(false)
        }
    }

    private fun facebookAuthUser(userInfo: UserInfo){
        val auth = FirebaseAuth.getInstance()
        val facebookAuthCredential = FacebookAuthProvider.getCredential(userInfo.getAccessToken()!!)
        auth.signInWithCredential(facebookAuthCredential).addOnCompleteListener {
            if (it.isSuccessful){
                val user = auth.currentUser
                Utils.showShortToast(mPresenter.getActivityContext(),"Success sign in!")
                addUser(userInfo)
            } else {
                mPresenter.showProgress(false)
                Utils.showShortToast(mPresenter.getActivityContext(),"Error while sign in!")
            }
        }
    }

    private fun googleAuthUser(userInfo: UserInfo){
        val auth = FirebaseAuth.getInstance()
        val googleAuthCredential = GoogleAuthProvider.getCredential(userInfo.getAccessToken()!!,null)
        auth.signInWithCredential(googleAuthCredential).addOnCompleteListener {
            if (it.isSuccessful){
                val user = auth.currentUser
                Utils.showShortToast(mPresenter.getActivityContext(),"Success sign in!")
                addUser(userInfo)
            } else {
                mPresenter.showProgress(false)
                Utils.showShortToast(mPresenter.getActivityContext(),"Error while sign in!")
            }
        }
    }

    private fun addUser(userInfo: UserInfo){
        val id = mDatabaseInstance.push().key
        if (id != null){
            userInfo.setId(id)
            userInfo.setArrayOfInteractions(ArrayList<String>())
            mDatabaseInstance.child(id).setValue(userInfo).addOnSuccessListener {
                Utils.showShortToast(mPresenter.getActivityContext(),"Welcome, " + userInfo.getFirstName()!!)
                addFirebaseTokenToDatabase(userInfo.getId()!!)
                savePreferences(userInfo)
            }.addOnFailureListener {
                Utils.showShortToast(mPresenter.getActivityContext(),it.message!!)
                Log.d("Login",it.message)
                mPresenter.showProgress(false)
            }
        } else {
            mPresenter.showProgress(false)
            Utils.showShortToast(mPresenter.getActivityContext(),"No id found")
        }
    }

    private fun addFirebaseTokenToDatabase(userId : String){
        try{
            val databaseReference = FirebaseDatabase.getInstance().getReference("token")
            val userToken = UserToken(userId,Prefs.getString(mPresenter.getActivityContext(),
                NotificationContants.FIREBASE_TOKEN,"")!!)
            databaseReference.child(userId).setValue(userToken)
        } catch (e: Exception){
            Log.d("Login",e.toString())
        }
    }

    private fun savePreferences(userInfo: UserInfo){
        Prefs.setBoolean(mPresenter.getActivityContext(),UserConstants.KEY_IS_USER_SIGNED_IN,true)
        Prefs.setString(mPresenter.getActivityContext(),UserConstants.KEY_USER_EMAIL,userInfo.getEmail())
        Prefs.setString(mPresenter.getActivityContext(),UserConstants.KEY_USER_ID,userInfo.getId()!!)
        Prefs.setString(mPresenter.getActivityContext(),UserConstants.KEY_USER_NAME,userInfo.getFirstName() + " " + userInfo.getLastName())
        Prefs.setObject(mPresenter.getActivityContext(),UserConstants.KEY_USER_INFO,userInfo)
        (mPresenter.getActivityContext() as LoginActivity).sendPushNotification(Prefs.getString(mPresenter.getActivityContext(),NotificationContants.FIREBASE_TOKEN,"")!!,"Hi, " + Prefs.getString(mPresenter.getActivityContext(),UserConstants.KEY_USER_NAME,""))
        mPresenter.showProgress(false)
        mPresenter.getActivityContext().startActivity(HomeActivity.getIntent(mPresenter.getActivityContext() as Activity,userInfo))
        (mPresenter.getActivityContext() as Activity).finish()
    }

}