package com.sandeepsingh.imageupload.feature.notification

import java.io.Serializable

/**
 * Created by Sandeep on 10/27/18.
 */
class UserToken() : Serializable {
    var userId : String ?= null
    var firebaseToken : String ?= null

    constructor(userId : String,firebaseToken : String) : this(){
        this.userId = userId
        this.firebaseToken = firebaseToken
    }
}