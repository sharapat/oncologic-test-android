package uz.tuit.oncologic.data.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import uz.tuit.oncologic.helper.SharedPreferencesHelper

class AddCookiesInterceptor(private val preferencesHelper: SharedPreferencesHelper) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val preferences = preferencesHelper.getCookies() as HashSet<String>
        preferences.forEach {
            builder.addHeader("Cookie", it)
        }

        return chain.proceed(builder.build())
    }
}