package uz.tuit.oncologic.data

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import uz.tuit.oncologic.data.model.AnswerModel
import uz.tuit.oncologic.data.model.QuestionModel
import uz.tuit.oncologic.data.network.ApiService
import uz.tuit.oncologic.helper.SharedPreferencesHelper
import uz.tuit.oncologic.helper.holder.QuestionsHolder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AppRepository(
    private val firestore: FirebaseFirestore,
    private val apiService: ApiService,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) {

    fun getQuestions(isMan: Boolean) : Single<List<QuestionModel>> {
        return when (isMan) {
            true -> getQuestionsFromCollection("questions")
            false -> getQuestionsFromCollection("question_woman")
        }
    }

    private fun getQuestionsFromCollection(collectionName: String) : Single<List<QuestionModel>> = Single.create { emitter ->
        firestore.collection(collectionName)
            .orderBy("id", Query.Direction.ASCENDING)
            .get()
            .addOnCompleteListener { task ->
                when (task.isSuccessful && task.result != null) {
                    true -> {
                        try {
                            val result: MutableList<QuestionModel> = mutableListOf()
                            task.result!!.documents.forEach { doc ->
                                val model = doc.toObject(QuestionModel::class.java)!!
                                if (model.type == QuestionModel.TYPE_RADIO) {
                                    model.answers = model.answers.toList().sortedBy { (_, value) ->
                                        value
                                    }.toMap()
                                }
                                result.add(model)
                            }
                            QuestionsHolder.questionList = result
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

    fun saveUser(user: HashMap<String, Any?>) : Single<String> {
        val compositeDisposable = CompositeDisposable()
        return Single.create { emitter ->

            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
            val birthday = Timestamp(sdf.parse(user["birthday"].toString()))
            user["birthday"] = birthday

            val userId = UUID.randomUUID().toString()
            firestore.document("polls/$userId")
                .set(user)
                .addOnCompleteListener { task ->
                    when (task.isSuccessful) {
                        true -> {
                            sharedPreferencesHelper.setUserName(user["name"].toString())
                            val requestMap: HashMap<String, RequestBody> = HashMap()
                            val person = RequestBody.create(MediaType.parse("text/plain"), user["name"].toString())
                            val gender = RequestBody.create(MediaType.parse("text/plain"), when (user["gender"] as Boolean) {
                                true -> "man"
                                false -> "woman"
                            })
                            val location = RequestBody.create(MediaType.parse("text/plain"), user["location"].toString())
                            val birthday1 = RequestBody.create(MediaType.parse("text/plain"), user["birthday"].toString())
                            val handler = RequestBody.create(MediaType.parse("text/plain"), "QuestionaryAnswer")
                            val module = RequestBody.create(MediaType.parse("text/plain"), "Questionary")
                            requestMap["person"] = person
                            requestMap["gender"] = gender
                            requestMap["location"] = location
                            requestMap["birthday"] = birthday1
                            requestMap["handler"] = handler
                            requestMap["module"] = module

                            compositeDisposable.add(
                                apiService.getResults(requestMap)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    emitter.onSuccess(userId)
                                }, {
                                    emitter.onError(it)
                                })
                            )
                        }
                        false -> emitter.onError(task.exception!!)
                    }
                }
        }
    }

    fun sendResults(userId: String, answers: Map<String, AnswerModel>) : Single<String> {
        val compositeDisposable = CompositeDisposable()
        return Single.create { emitter ->
            val batch: WriteBatch = firestore.batch()
            val ref: CollectionReference = firestore.collection("polls/$userId/answers")
            answers.forEach {
                val map: HashMap<String, Any> = HashMap()
                map["id"] = it.value.id
                map["question_id"] = it.value.questionId
                map["question_name"] = it.value.questionName
                map["text"] = it.value.text
                map["value"] = it.value.value
                batch.set(ref.document(it.key), map)
            }
            batch.commit().addOnCompleteListener { task ->
                when (task.isSuccessful) {
                    true -> {
                        val params: HashMap<String, RequestBody> = HashMap()

                        answers.forEach {
                            val data = RequestBody.create(MediaType.parse("text/plain"), it.value.value)
                            params[it.value.questionName] = data
                        }
                        val handler = RequestBody.create(MediaType.parse("text/plain"), "QuestionaryAnswer")
                        val module = RequestBody.create(MediaType.parse("text/plain"), "Questionary")
                        params["handler"] = handler
                        params["module"] = module
                        compositeDisposable.add(
                            apiService.getResults(params)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    emitter.onSuccess(it)
                                }, {
                                    emitter.onError(it)
                                })
                        )
                    }
                    false -> emitter.onError(task.exception!!)
                }
            }
        }
    }

    fun clearCookie() {
     //   sharedPreferencesHelper.clear()
    }

    fun getUsername() : String? {
        return sharedPreferencesHelper.getUserName()
    }

}