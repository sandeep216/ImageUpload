package com.sandeepsingh.imageupload.feature.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by Sandeep on 10/23/18.
 */
class PagerAdapter(fragmentManager: FragmentManager,val arrayList: ArrayList<Fragment>) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(p0: Int): Fragment {
        return arrayList[p0]
    }

    override fun getCount(): Int {
        return arrayList.size
    }
}