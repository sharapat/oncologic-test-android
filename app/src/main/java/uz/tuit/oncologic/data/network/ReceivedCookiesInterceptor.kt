package uz.tuit.oncologic.data.network

import okhttp3.Interceptor
import okhttp3.Response
import uz.tuit.oncologic.helper.SharedPreferencesHelper

class ReceivedCookiesInterceptor(private val preferencesHelper: SharedPreferencesHelper) : Interceptor {

    //Taken from https://gist.github.com/tsuharesu/cbfd8f02d46498b01f1b

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())

        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies: HashSet<String> = HashSet()

            originalResponse.headers("Set-Cookie").forEach {
                cookies.add(it)
            }

            preferencesHelper.setCookies(cookies)
        }
        return originalResponse
    }
}