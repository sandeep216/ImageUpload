package com.sandeepsingh.imageupload.core

import android.content.Context
import android.widget.Toast
import android.R.attr.x
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by Sandeep on 10/21/18.
 */
class Utils{

    companion object {
        fun showShortToast(context: Context, message : String){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }

        fun showLongToast(context: Context, message: String){
            Toast.makeText(context,message,Toast.LENGTH_LONG).show()
        }

        fun getDate(timeStamp : Long) : String{
            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val calendar = Calendar.getInstance()
            calendar.setTimeInMillis(timeStamp)
            return formatter.format(calendar.getTime())
        }

        fun haveNetworkConnection(context: Context): Boolean {
            var haveConnectedWifi = false
            var haveConnectedMobile = false

            val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val networks = connectivityManager.allNetworks
                var networkInfo: NetworkInfo
                for (mNetwork in networks) {
                    networkInfo = connectivityManager.getNetworkInfo(mNetwork)
                    if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                        if (networkInfo.typeName.equals("WIFI", ignoreCase = true)) {
                            haveConnectedWifi = true
                            if (haveConnectedMobile)
                                break
                        } else if (networkInfo.typeName.equals("MOBILE", ignoreCase = true)) {
                            haveConnectedMobile = true
                            if (haveConnectedWifi)
                                break
                        }
                    }
                }
            } else {
                if (connectivityManager != null) {

                    val info = connectivityManager.allNetworkInfo
                    if (info != null) {
                        for (networkInfo in info) {
                            if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                                if (networkInfo.typeName.equals("WIFI", ignoreCase = true)) {
                                    haveConnectedWifi = true
                                    if (haveConnectedMobile)
                                        break
                                } else if (networkInfo.typeName.equals("MOBILE", ignoreCase = true)) {
                                    haveConnectedMobile = true
                                    if (haveConnectedWifi)
                                        break
                                }
                            }
                        }
                    }
                }
            }
            return haveConnectedWifi || haveConnectedMobile
        }

        fun getRetrofitInstance(url :String): Retrofit {
            val _gson = GsonBuilder()
                .setDateFormat(BaseActivity.DATE_FORMAT)
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

                val request = chain.request().newBuilder().addHeader(BaseActivity.AUTHORIZATION, authKey).build()
                val response = chain.proceed(request)

                response
            }.connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
            val _builder = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(_gson))

            val client = httpClient.build()
            return _builder.client(client).build()
        }
    }

}