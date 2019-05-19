package uz.tuit.oncologic.helper

import android.content.Context

class SharedPreferencesHelper(context: Context) {

    companion object {
        const val FILE_NAME = "preferences"
        const val PREF_COOKIES = "pref_cookies"
    }

    private val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun setCookies(cookies: HashSet<String>) {
        preferences.edit().putStringSet(PREF_COOKIES, cookies).apply()
    }

    fun getCookies(): Set<String>? =
        preferences.getStringSet(PREF_COOKIES, HashSet<String>())

}