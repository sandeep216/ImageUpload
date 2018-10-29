package com.sandeepsingh.imageupload.core

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sandeepsingh.imageupload.feature.login.pojos.UserInfo


/**
 * Created by Sandeep on 10/24/18.
 */
class Prefs {
    private val TAG = Prefs::class.java.simpleName


    companion object {
        val IMAGE_UPLOAD_PREFS = "image_upload"

        /**
         *
         * Provides Shared object
         * @param context
         * @return
         */
        private fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(IMAGE_UPLOAD_PREFS, Context.MODE_PRIVATE)
        }

        /**
         *
         * Saves a string value for a given key in Shared
         * @param context
         * @param key
         * @param value
         */
        fun setString(context: Context, key: String, value: String) {
            getPrefs(context).edit().putString(key, value).apply()
        }

        /**
         *
         * Saves a string value for a given key in Shared
         * @param context
         * @param key
         * @param value
         */
        fun setObject(context: Context, key: String, value: Any){
            val gson = Gson()
            val json = gson.toJson(value)
            getPrefs(context).edit().putString(key,json).apply()
        }

        /**
         *
         * Saves integer value for a given key in Shared
         * @param context
         * @param key
         * @param value
         */
        fun setInt(context: Context, key: String, value: Int) {
            getPrefs(context).edit().putInt(key, value).apply()
        }

        /**
         *
         * Saves float value for a given key in Shared
         * @param context
         * @param key
         * @param value
         */
        fun setFloat(context: Context, key: String, value: Float) {
            getPrefs(context).edit().putFloat(key, value).apply()
        }

        /**
         *
         * Saves long value for a given key in Shared
         * @param context
         * @param key
         * @param value
         */
        fun setLong(context: Context, key: String, value: Long) {
            getPrefs(context).edit().putLong(key, value).apply()
        }

        /**
         *
         * Saves boolean value for a given key in Shared
         * @param context
         * @param key
         * @param value
         */
        fun setBoolean(context: Context, key: String, value: Boolean) {
            getPrefs(context).edit().putBoolean(key, value).apply()
        }

        /**
         * Provides string from the Shared Preferences
         * @param context
         * @param key
         * @param defaultValue
         * @return
         */
        fun getString(context: Context, key: String, defaultValue: String): String? {
            return getPrefs(context).getString(key, defaultValue)
        }

        /**
         * Provides int from Shared Preferences
         * @param context
         * @param key
         * @param defaultValue
         * @return
         */
        fun getInt(context: Context, key: String, defaultValue: Int): Int {
            return getPrefs(context).getInt(key, defaultValue)
        }

        /**
         * Provides boolean from Shared Preferences
         * @param context
         * @param key
         * @param defaultValue
         * @return
         */
        fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
            return getPrefs(context).getBoolean(key, defaultValue)
        }

        /**
         * Provides float value from Shared Preferences
         * @param context
         * @param key
         * @param defaultValue
         * @return
         */
        fun getFloat(context: Context, key: String, defaultValue: Float): Float {
            return getPrefs(context).getFloat(key, defaultValue)
        }

        /**
         * Provides long value from Shared Preferences
         * @param context
         * @param key
         * @param defaultValue
         * @return
         */
        fun getLong(context: Context, key: String, defaultValue: Long): Long {
            return getPrefs(context).getLong(key, defaultValue)
        }

        /**
         * Clears all the shared preference data from styfi file
         * @param context
         */
        fun clearPrefs(context: Context) {
            getPrefs(context).edit().clear().commit()
        }

        /**
         * Clears a key-value pair from shared preference data from styfi file
         * @param context
         * @param key
         */
        fun clearPrefs(context: Context, key: String) {
            getPrefs(context).edit().remove(key).apply()
        }
    }

}