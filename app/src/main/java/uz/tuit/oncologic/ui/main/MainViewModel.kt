package uz.tuit.oncologic.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import uz.tuit.oncologic.data.AppRepository
import uz.tuit.oncologic.data.model.QuestionModel
import uz.tuit.oncologic.data.model.Resource
import uz.tuit.oncologic.holder.QuestionsHolder

class MainViewModel(private val repository: AppRepository): ViewModel() {

    var pageNumber = 0
    val questions = MutableLiveData<Resource<List<QuestionModel>>>()
    val compositeDisposable by lazy { CompositeDisposable() }

    fun getNextQuestions() {
        pageNumber ++
        val questionList = QuestionsHolder.questionList.filter { it.page == pageNumber }
        when (questionList.isNullOrEmpty()) {
            true -> questions.value = Resource.error("End of questions")
            false -> questions.value = Resource.success(questionList)
        }
    }

    fun sendResults(results: Map<String, String>) {
        compositeDisposable.add()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}