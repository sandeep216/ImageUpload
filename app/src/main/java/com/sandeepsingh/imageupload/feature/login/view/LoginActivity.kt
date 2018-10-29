package com.sandeepsingh.imageupload.feature.login.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sandeepsingh.imageupload.R
import com.sandeepsingh.imageupload.core.BaseActivity
import com.sandeepsingh.imageupload.core.LoginConstants
import com.sandeepsingh.imageupload.core.Utils
import com.sandeepsingh.imageupload.feature.login.ILogin
import com.sandeepsingh.imageupload.feature.login.LoginCallback
import com.sandeepsingh.imageupload.feature.login.SocialManagement
import com.sandeepsingh.imageupload.feature.login.model.LoginModel
import com.sandeepsingh.imageupload.feature.login.pojos.UserInfo
import com.sandeepsingh.imageupload.feature.login.presenter.LoginPresenter
import kotlinx.android.synthetic.main.fragment_feed.*

class LoginActivity : BaseActivity(), ILogin.PresenterToView, LoginCallback, View.OnClickListener {

    lateinit var ivFacebookLogin : ImageView

    lateinit var ivGoogleLogin : ImageView

    lateinit var mPresenter: LoginPresenter

    lateinit var mSocialManagement: SocialManagement

    lateinit var mDatabaseReference: DatabaseReference

    lateinit var loaderView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initComponents()
        setupMvp()
    }

    override fun onClick(view : View){
        when(view.id){
            R.id.iv_facebook -> {
                if (Utils.haveNetworkConnection(this)) {
                    showProgress(true)
                    mSocialManagement = SocialManagement(LoginConstants.PROVIDER_FACEBOOK, this, this)
                    mSocialManagement.login()
                } else {
                    Utils.showShortToast(this,"No internet connection!")
                }
            }

            R.id.iv_google ->{
                if (Utils.haveNetworkConnection(this)) {
                    showProgress(true)
                    mSocialManagement = SocialManagement(LoginConstants.PROVIDER_GOOGLE, this, this)
                    mSocialManagement.login()
                } else {
                    Utils.showShortToast(this,"No internet connection!")
                }
            }
        }
    }

    fun initComponents(){
        ivFacebookLogin = findViewById(R.id.iv_facebook)
        ivGoogleLogin = findViewById(R.id.iv_google)
        ivGoogleLogin.setOnClickListener(this)
        ivFacebookLogin.setOnClickListener(this)
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        loaderView = findViewById(R.id.loader)
    }

    fun setupMvp(){
        mPresenter = LoginPresenter(this)
        val model = LoginModel(mPresenter, mDatabaseReference)
        mPresenter.setModel(model)
    }

    override fun getAppContext(): Context {
        return applicationContext
    }

    override fun getActivityContext(): Context {
        return this
    }

    override fun onCompleteLogin(isError: Boolean, tag: String, message: String?, userInfo: UserInfo?) {
        if (!isError && userInfo != null){
            mPresenter.onLogin(userInfo)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            mSocialManagement.onSocialResult(requestCode, resultCode, data!!)
           // finish()
        } else {
            showProgress(false)
           Utils.showShortToast(this," Something went wrong!")
        }
    }

    override fun showProgress(isVisible : Boolean){
        showProgressDialog(loaderView,isVisible)
    }
}
