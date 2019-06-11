package uz.tuit.oncologic.helper

import android.content.Context

class SharedPreferencesHelper(context: Context) {

    companion object {
        const val FILE_NAME = "preferences"
        const val USER_NAME = "user_name"
        const val USER_GENDER = "user_gender"
    }

    private val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun clear() {
        preferences.edit().clear().apply()
    }

    fun setUserName(name: String) {
        preferences.edit().putString(USER_NAME, name).apply()
    }

    fun getUserName(): String? {
        return preferences.getString(USER_NAME, "")
    }

    fun setUserGender(gender: Boolean) {
        preferences.edit().putBoolean(USER_GENDER, gender).apply()
    }

    fun getUserGender() : Boolean =
            preferences.getBoolean(USER_GENDER, true)
}