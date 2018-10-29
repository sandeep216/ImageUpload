package com.sandeepsingh.imageupload.feature.login

import com.sandeepsingh.imageupload.core.IContext
import com.sandeepsingh.imageupload.feature.login.pojos.UserInfo

/**
 * Created by Sandeep on 10/22/18.
 */
interface ILogin {

    interface ViewToPresenter{
        fun onLogin(userInfo: UserInfo)
    }

    interface PresenterToModel{
        fun onLogin(userInfo: UserInfo)
    }

    interface ModelToPresenter : IContext{

        fun showProgress(isVisible: Boolean)
    }

    interface PresenterToView : IContext{

        fun showProgress(isVisible: Boolean)
    }
}