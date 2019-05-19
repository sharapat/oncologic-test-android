package uz.tuit.oncologic.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import uz.tuit.oncologic.data.AppRepository
import uz.tuit.oncologic.data.model.QuestionModel
import uz.tuit.oncologic.data.model.Resource

class AuthViewModel(private val repository: AppRepository): ViewModel() {

    val questionList = MutableLiveData<Resource<List<QuestionModel>>>()
    val startTestResult = MutableLiveData<Resource<String>>()
    private val compositeDisposable by lazy { CompositeDisposable() }

    fun requestTestQuestions(isMan: Boolean) {
        compositeDisposable.add(
            repository.getQuestions(isMan)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    questionList.value = Resource.loading()
                }
                .subscribe({ result ->
                    questionList.value = Resource.success(result)
                }, {
                    questionList.value = Resource.error("Ошибка при загрузки анкеты. ${it.localizedMessage}")
                })
        )
    }

    fun saveUser(user: HashMap<String, Any?>) {
        compositeDisposable.add(
            repository.saveUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    startTestResult.value = Resource.loading()
                }
                .subscribe({ result ->
                    startTestResult.value = Resource.success(result)
                }, {
                    startTestResult.value = Resource.error("Произошла ошибка. ${it.localizedMessage}")
                })
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}