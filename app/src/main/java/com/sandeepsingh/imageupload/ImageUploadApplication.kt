package com.sandeepsingh.imageupload

import android.app.Application
import android.content.Context
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.FirebaseApp
import com.google.android.gms.common.util.IOUtils.toByteArray
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.support.multidex.MultiDexApplication
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import android.support.multidex.MultiDex


/**
 * Created by Sandeep on 10/22/18.
 */
class ImageUploadApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(this)
        FirebaseApp.initializeApp(this)

        try {
            val info = packageManager.getPackageInfo(
                "com.sandeepsingh.imageupload",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}