package com.sandeepsingh.imageupload.core

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Sandeep on 10/26/18.
 */
interface IRetrofitCalls {

    @POST("fcm/send")
    @Headers("Content-Type: application/json")
    fun pushNotification(@Body jsonObject: JsonObject) : Call<JsonElement>

    @GET("comments.json")
    fun getComments(@Query("orderBy") orderBy : String,@Query("startAt") startAt : String) : Call<JsonElement>
}