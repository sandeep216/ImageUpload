package com.sandeepsingh.imageupload.feature.comment.pojos

import java.io.Serializable

/**
 * Created by Sandeep on 10/24/18.
 */
class Comment() : Serializable {

    var commentId : String?=null
    var comment : String?=null
    var commentBy : String?=null
    var commentById : String ?=null
    var commentAt : Long?=null
    var imageId : String?=null

    constructor(commentId : String,comment:String,commentBy:String,commentById : String,commentAt: Long,imageId:String):this(){
        this.commentId = commentId
        this.comment = comment
        this.commentBy = commentBy
        this.commentById = commentById
        this.commentAt = commentAt
        this.imageId = imageId
    }
}