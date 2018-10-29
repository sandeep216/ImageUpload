package com.sandeepsingh.imageupload.feature.home.feed.pojos

import java.io.Serializable

/**
 * Created by Sandeep on 10/24/18.
 */
class Image() : Serializable {

    var imageId: String? = null
    var imageUrl: String? = null
    var userId: String? = null
    var timeStamp : Long? = null
    var imageUploadedByName : String?=null

    constructor(imageId: String, imageUrl: String, userId: String, imageUploadByName : String) : this() {
        this.imageId = imageId
        this.imageUrl = imageUrl
        this.userId = userId
        timeStamp = System.currentTimeMillis()
        this.imageUploadedByName = imageUploadByName
    }
}