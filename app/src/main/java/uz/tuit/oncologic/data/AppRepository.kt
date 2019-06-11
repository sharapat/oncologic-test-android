package uz.tuit.oncologic.data

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import io.reactivex.Single
import org.koin.ext.castValue
import org.koin.ext.checkedStringValue
import uz.tuit.oncologic.data.model.AnswerModel
import uz.tuit.oncologic.data.model.QuestionModel
import uz.tuit.oncologic.data.model.UserModel
import uz.tuit.oncologic.helper.SharedPreferencesHelper
import uz.tuit.oncologic.helper.holder.QuestionsHolder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AppRepository(
    private val firestore: FirebaseFirestore,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) {

    fun getQuestions(isMan: Boolean) : Single<List<QuestionModel>> {
        return when (isMan) {
            true -> getQuestionsFromCollection("questions")
            false -> getQuestionsFromCollection("questions_woman")
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
                                model.is_required = when(doc.get("is_required")) {
                                    false -> false
                                    else -> true
                                }
                                if (model.type == QuestionModel.TYPE_RADIO) {
                                    model.answers = model.answers.toList().sortedBy { (_, value) ->
                                        value
                                    }.toMap()
                                }
                                result.add(model)
                            }
                            var total = 0
                            result.forEach {
                                if (it.is_required) {
                                    val array: MutableList<Int> = arrayListOf()
                                    it.answers.forEach { answer ->
                                        array.add(answer.value.toInt())
                                    }
                                    array.sortDescending()
                                    total += array[0]
                                }
                            }
                            Log.d("toliq", total.toString())
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
                            sharedPreferencesHelper.setUserGender(user["gender"].toString().toBoolean())
                            emitter.onSuccess("user saved")
                        }
                        false -> emitter.onError(task.exception!!)
                    }
                }
        }
    }

    fun sendResults(userId: String, answers: Map<String, AnswerModel>) : Single<String> {
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
                        emitter.onSuccess("success")
                    }
                    false -> emitter.onError(task.exception!!)
                }
            }
        }
    }

    fun clearCookie() {
        sharedPreferencesHelper.clear()
    }

    fun getUser() : UserModel {
        val user = UserModel()
        user.name = sharedPreferencesHelper.getUserName()!!
        user.gender = sharedPreferencesHelper.getUserGender()
        return user
    }

}