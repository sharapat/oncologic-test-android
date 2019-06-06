package uz.tuit.oncologic.data.network

import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @Headers("User-Agent:Chrome/51.0.2704.103")
    @Multipart
    @POST("/test/")
    fun getResults(@PartMap params: HashMap<String, RequestBody>) : Single<String>
}