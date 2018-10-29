package com.sandeepsingh.imageupload.core

import java.lang.ref.WeakReference

/**
 * Created by Sandeep on 10/21/18.
 */
open class BasePresenter<T>(view: T) {
    protected var mView: WeakReference<T>? = null

    init {
        mView = WeakReference(view)
    }

    protected fun setView(view: T) {
        mView = WeakReference(view)
    }

    @Throws(NullPointerException::class)
    protected fun getView(): T? {
        return if (mView != null)
            mView!!.get()
        else
            throw NullPointerException("View is unavailable")
    }

    protected fun onDestroy() {
        mView = null
    }
}