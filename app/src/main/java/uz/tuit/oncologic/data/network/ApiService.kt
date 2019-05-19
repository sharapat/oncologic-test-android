package uz.tuit.oncologic.data.network

import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.PartMap

interface ApiService {

    @POST("/test/")
    fun getResults(@PartMap params: Map<String, String>) : Single<String>
}