package com.sandeepsingh.imageupload.feature.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sandeepsingh.imageupload.R
import com.sandeepsingh.imageupload.core.NotificationContants
import com.sandeepsingh.imageupload.core.Prefs
import com.sandeepsingh.imageupload.feature.splash.SplashScreen

/**
 * Created by Sandeep on 10/26/18.
 */
class NotificationListener : FirebaseMessagingService() {

    var refreshedToken = ""
    private var mNotificationManager: NotificationManager? = null
    private val CHANNEL_DEFAULT_NAME = "COMMENT"

    override fun onCreate() {
        super.onCreate()
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mNotificationManager != null) mNotificationManager!!.createNotificationChannel(NotificationChannel(resources.getString(
                R.string.default_notification_channel_id), CHANNEL_DEFAULT_NAME, NotificationManager.IMPORTANCE_DEFAULT))
        }
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        refreshedToken = p0!!
        Log.i("Login","Token" +  p0)
        Prefs.setString(this,NotificationContants.FIREBASE_TOKEN,refreshedToken)
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        val resultIntent = Intent(applicationContext, SplashScreen::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
       createNotification(p0!!.notification!!.title!!,p0.notification!!.body!!,pendingIntent)
    }

    private fun createNotification(title: String, body: String?, intent: PendingIntent) {

        val context = baseContext

        val mBuilder = NotificationCompat.Builder(context, resources.getString(R.string.default_notification_channel_id))
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) R.drawable.ic_profile_icon_filled else R.mipmap.ic_launcher)
            .setColor(Color.BLACK)
            .setWhen(System.currentTimeMillis())
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(intent)
            .setLights(Color.CYAN, 3000, 3000)
            .setPriority(Notification.PRIORITY_HIGH)
            .setAutoCancel(true)

            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            mBuilder.setVibrate(longArrayOf(500, 500))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(getString(R.string.default_notification_channel_id),
                "COMMENT",
                NotificationManager.IMPORTANCE_DEFAULT)
            mNotificationManager!!.createNotificationChannel(channel)
        }

        if (mNotificationManager != null)
            mNotificationManager!!.notify(100, mBuilder.build())
    }
}