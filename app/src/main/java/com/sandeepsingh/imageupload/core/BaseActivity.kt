package com.sandeepsingh.imageupload.core

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

/**
 * Created by Sandeep on 10/21/18.
 */
abstract class BaseActivity : AppCompatActivity() {

    companion object {
        val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        val AUTHORIZATION = "Authorization"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showProgressDialog(progressBar: View, isVisible: Boolean) {
        if (isVisible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    fun sendPushNotification(userToken: String, message: String) {
        try {
            val iRetrofitCalls = getRetrofitInstance().create(IRetrofitCalls::class.java)
            val call = iRetrofitCalls.pushNotification(createNotificationData(userToken, message))
            val callback = object : Callback<JsonElement>{
                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    Log.i("Login","Response " + t.toString())
                }

                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    Log.i("Login","Failure" + response.body().toString())
                }
            }
            call.enqueue(callback)
        } catch (e: Exception) {
            Log.i("Login","Exception" + e.toString())
        }
    }

    fun createNotificationData(userToken: String,message: String) : JsonObject{
        val notificationJson = JsonObject()
        val notificationData = JsonObject()
        notificationData.add("body",JsonPrimitive(message))
        notificationData.add("title",JsonPrimitive("Notification"))
        notificationJson.add("notification",notificationData)
        notificationJson.add("to",JsonPrimitive(userToken))
        return notificationJson
    }

    fun getRetrofitInstance(): Retrofit {
        val _gson = GsonBuilder()
            .setDateFormat(DATE_FORMAT)
            .create()

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            var authKey: String
            try {
                authKey = "key=" + NotificationContants.AUTHKEY
            } catch (e: Exception) {
                e.printStackTrace()
                authKey = ""
            }

            val request = chain.request().newBuilder().addHeader(AUTHORIZATION, authKey).build()
            val response = chain.proceed(request)

            response
        }.connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
        val _builder = Retrofit.Builder()
            .baseUrl(NotificationContants.FIREBASE_PUSH_API_URL)
            .addConverterFactory(GsonConverterFactory.create(_gson))

        val client = httpClient.build()
        return _builder.client(client).build()
    }
}