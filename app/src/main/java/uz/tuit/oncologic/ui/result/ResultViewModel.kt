package uz.tuit.oncologic.ui.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.tuit.oncologic.data.AppRepository
import uz.tuit.oncologic.data.model.Resource
import uz.tuit.oncologic.data.model.UserModel

class ResultViewModel(private val repository: AppRepository) : ViewModel() {
    val userInfo = MutableLiveData<Resource<UserModel>>()
    val result = MutableLiveData<Resource<String>>()

    fun getResults(ball: Int) {

        userInfo.value = Resource.success(repository.getUser())
        result.value = Resource.error(ball.toString())
    }

    fun clearData() {
        repository.clearCookie()
    }
}