package uz.tuit.oncologic.data.network

import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PartMap

interface ApiService {

    @Multipart
    @POST("/test/")
    fun getResults(@PartMap params: HashMap<String, RequestBody>) : Single<String>
}