package com.sandeepsingh.imageupload.core

import android.content.Context

/**
 * Created by Sandeep on 10/22/18.
 */
interface IContext {
    fun getAppContext(): Context
    fun getActivityContext(): Context
}