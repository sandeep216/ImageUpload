package com.sandeepsingh.imageupload.feature.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.sandeepsingh.imageupload.R
import com.sandeepsingh.imageupload.core.BaseActivity
import com.sandeepsingh.imageupload.core.LoginConstants
import com.sandeepsingh.imageupload.core.Prefs
import com.sandeepsingh.imageupload.core.UserConstants
import com.sandeepsingh.imageupload.feature.home.HomeActivity
import com.sandeepsingh.imageupload.feature.login.pojos.UserInfo
import com.sandeepsingh.imageupload.feature.login.view.LoginActivity

/**
 * Created by Sandeep on 10/24/18.
 */
class SplashScreen : BaseActivity() {
    // Splash screen timer
    val SPLASH_TIME_OUT = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Log.d("Login","User " + FirebaseAuth.getInstance().getCurrentUser())

        Handler().postDelayed( {
            if (Prefs.getBoolean(this,UserConstants.KEY_IS_USER_SIGNED_IN,false)){
                val gson = Gson()
                val userInfo = gson.fromJson(Prefs.getString(this,UserConstants.KEY_USER_INFO,""),UserInfo::class.java)
                fetchUserInteractedData(userInfo)
            } else {
                val i = Intent(this@SplashScreen, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
        }, SPLASH_TIME_OUT)
    }

    private fun fetchUserInteractedData(userInfo : UserInfo){
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        val query = databaseReference.orderByChild("id").startAt(Prefs.getString(this,UserConstants.KEY_USER_ID,"")).endAt(Prefs.getString(this,UserConstants.KEY_USER_ID,""))
        query.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.children.iterator().next().getValue(UserInfo ::class.java)
                var interactionArray = (p0.children.iterator().next().value as Map<String,ArrayList<String>>).get("arrayOfInteraction")
                if (interactionArray == null){
                    interactionArray = ArrayList()
                }
                user!!.setArrayOfInteractions(interactionArray)
                Prefs.setObject(this@SplashScreen,UserConstants.KEY_USER_INFO,user)
                query.removeEventListener(this)
                val i = Intent(HomeActivity.getIntent(this@SplashScreen,user))
                startActivity(i)
                finish()
            }
        })
    }
}