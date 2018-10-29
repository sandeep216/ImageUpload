package com.sandeepsingh.imageupload.core

/**
 * Created by Sandeep on 10/22/18.
 */

open class LoginConstants{

    companion object {
        val PROVIDER_GOOGLE = 0x0FEE07
        val PROVIDER_FACEBOOK = 0x0FEE01
        val P_FACEBOOK = "facebook"
        val P_GOOGLE = "google"
        val GOOGLE_WEBCLIENT_ID = "Your web client id"
    }
}

open class UserConstants{
    companion object {
        val KEY_IS_USER_SIGNED_IN = "is_user_signed_in"
        val KEY_USER_NAME = "user_name"
        val KEY_USER_EMAIL = "user_email"
        val KEY_USER_ID = "user_id"
        val KEY_USER_INFO = "user_info"
    }
}

open class NotificationContants{
    companion object {
        val FIREBASE_PUSH_API_URL = "https://fcm.googleapis.com/"
        val FIREBASE_TOKEN = "firebase_token"
        val AUTHKEY = "firebase server key"
    }
}