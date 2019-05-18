package uz.pos.botpro.data

import io.reactivex.Observable
import uz.tuit.oncologic.data.model.MainInfoPayload

class OncologicTestRepository(private val apiService: ApiService) {

    fun getMainInfo(): Observable<GenericResponse<MainInfoPayload>> {
        return apiService.getMainInfo()
    }
}