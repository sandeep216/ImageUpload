package com.sandeepsingh.imageupload.feature.login

import com.sandeepsingh.imageupload.feature.login.pojos.UserInfo

/**
 * Created by Sandeep on 10/22/18.
 */
interface LoginCallback {
        fun onCompleteLogin(isError: Boolean, tag: String, message: String?, userInfo: UserInfo?)
    }