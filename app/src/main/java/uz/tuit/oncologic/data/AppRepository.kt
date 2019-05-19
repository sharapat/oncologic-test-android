package uz.tuit.oncologic.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.reactivex.Single
import uz.tuit.oncologic.data.model.QuestionModel
import uz.tuit.oncologic.data.network.ApiService

class AppRepository(
    private val firestore: FirebaseFirestore,
    private val apiService: ApiService
) {

    fun getQuestions(isMan: Boolean) : Single<List<QuestionModel>> {
        return Single.create { emitter ->
            when (isMan) {
                true -> {
                    firestore.collection("questions")
                        .orderBy("id", Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener { task ->
                            when (task.isSuccessful && task.result != null) {
                                true -> {
                                    try {
                                        val result: MutableList<QuestionModel> = arrayListOf()
                                        task.result!!.documents.forEach { doc ->
                                            result.add(doc.toObject(QuestionModel::class.java)!!)
                                        }
                                        emitter.onSuccess(result)
                                    } catch (e: Exception) {
                                        emitter.onError(e)
                                    }
                                }
                                false -> {
                                    emitter.onError(task.exception!!)
                                }
                            }
                        }
                }
                false -> {
                    firestore.collection("questions_woman")
                        .orderBy("id", Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener { task ->
                            when (task.isSuccessful && task.result != null) {
                                true -> {
                                    try {
                                        val result: MutableList<QuestionModel> = arrayListOf()
                                        task.result!!.documents.forEach { doc ->
                                            result.add(doc.toObject(QuestionModel::class.java)!!)
                                        }
                                        emitter.onSuccess(result)
                                    } catch (e: Exception) {
                                        emitter.onError(e)
                                    }
                                }
                                false -> {
                                    emitter.onError(task.exception!!)
                                }
                            }
                        }
                }
            }
        }
    }

    fun getResults(params: Map<String, String>) : Single<String> =
            apiService.getResults(params)


}