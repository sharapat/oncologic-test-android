package uz.tuit.oncologic.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.reactivex.Single
import uz.tuit.oncologic.data.model.QuestionModel
import uz.tuit.oncologic.data.network.ApiService
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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

    fun saveUser(user: HashMap<String, Any?>) : Single<String> {
        return Single.create { emitter ->

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val birthdate = Timestamp(sdf.parse(user["birthdate"].toString()))
            user["birthdate"] = birthdate

            val userId = UUID.randomUUID().toString()
            firestore.document("polls/$userId")
                .set(user)
                .addOnCompleteListener { task ->
                    when (task.isSuccessful) {
                        true -> {
                            val requestMap: HashMap<String, String> = HashMap()
                            requestMap["person"] = user["name"].toString()
                            requestMap["gender"] = when (user["gender"] as Boolean) {
                                true -> "man"
                                false -> "woman"
                            }
                            requestMap["location"] = user["location"].toString()
                            requestMap["birthday"] = user["birthday"].toString()
                            sendResults(requestMap)
                            emitter.onSuccess(userId)
                        }
                        false -> emitter.onError(task.exception!!)
                    }
                }
        }
    }


    fun sendResults(params: Map<String, String>) : Single<String> =
            apiService.getResults(params)
}