package com.sandeepsingh.imageupload.feature.login.presenter

import android.content.Context
import com.sandeepsingh.imageupload.core.BasePresenter
import com.sandeepsingh.imageupload.feature.login.ILogin
import com.sandeepsingh.imageupload.feature.login.pojos.UserInfo

/**
 * Created by Sandeep on 10/22/18.
 */
class LoginPresenter(var view : ILogin.PresenterToView) : BasePresenter<ILogin.PresenterToView>(view),ILogin.ViewToPresenter,ILogin.ModelToPresenter{

    lateinit var mModel : ILogin.PresenterToModel

    override fun onLogin(userInfo: UserInfo) {
        mModel.onLogin(userInfo)
    }

    override fun getAppContext(): Context {
        return view.getAppContext()
    }

    override fun getActivityContext(): Context {
        return view.getActivityContext()
    }

    fun setModel(model: ILogin.PresenterToModel) {
        this.mModel = model
    }

    override fun showProgress(isVisible : Boolean){
        getView()!!.showProgress(isVisible)
    }

}