package uz.tuit.oncologic.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import uz.tuit.oncologic.data.AppRepository
import uz.tuit.oncologic.data.model.AnswerModel
import uz.tuit.oncologic.data.model.QuestionModel
import uz.tuit.oncologic.data.model.Resource
import uz.tuit.oncologic.helper.holder.QuestionsHolder

class MainViewModel(private val repository: AppRepository): ViewModel() {

    var pageNumber = 0
    val questions = MutableLiveData<Resource<List<QuestionModel>>>()
    val requestResults = MutableLiveData<Resource<String>>()
    private val compositeDisposable by lazy { CompositeDisposable() }

    fun getNextQuestions() {
        questions.value = Resource.loading()
        pageNumber ++
        val questionList = QuestionsHolder.questionList.filter { it.page == pageNumber }
        when (questionList.isNullOrEmpty()) {
            true -> questions.value = Resource.error("End of questions")
            false -> questions.value = Resource.success(questionList)
        }
    }

    fun sendResults(userId: String, results: Map<String, AnswerModel>) {
        compositeDisposable.add(
            repository.sendResults(userId, results)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    requestResults.postValue(Resource.loading())
                }
                .subscribe({
                    requestResults.postValue(Resource.success(it))
                }, {
                    requestResults.postValue(Resource.error("Ошибка при отправки ответов. ${it.localizedMessage}"))
                })
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun restart() {
        repository.clearCookie()
    }
}