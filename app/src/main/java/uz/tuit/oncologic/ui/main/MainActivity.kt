package uz.tuit.oncologic.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import uz.tuit.oncologic.R
import uz.tuit.oncologic.data.model.AnswerModel
import uz.tuit.oncologic.data.model.QuestionModel
import uz.tuit.oncologic.data.model.Status
import uz.tuit.oncologic.extensions.visibility
import uz.tuit.oncologic.ui.BaseActivity
import uz.tuit.oncologic.ui.auth.AuthActivity
import uz.tuit.oncologic.ui.custom.CustomQuestionItem
import uz.tuit.oncologic.ui.main.list.OnAnswerSelectedListener
import uz.tuit.oncologic.ui.result.ResultActivity
import kotlin.Exception

class MainActivity : BaseActivity(), OnAnswerSelectedListener {

    private val viewModel: MainViewModel by viewModel()
    private var questionsCount = 0
    private var userId: String = ""
    private val answersMap: HashMap<String, AnswerModel> = HashMap()
    private var ball = 0

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userId = intent.getStringExtra(AuthActivity.USER_ID)

        firestore.collection("questions").orderBy("id", Query.Direction.ASCENDING).get().addOnCompleteListener {
            it.result!!.documents.forEachIndexed { index, documentSnapshot ->
                firestore.document("questions_woman/question_${index+1}").set(documentSnapshot.data!!)
            }
        }

        viewModel.questions.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> {
                    if (it.message == "End of questions") {
                        viewModel.sendResults(userId, answersMap)
                    } else {
                        toast(it.message)
                        progressBar?.visibility(false)
                    }
                }
                Status.SUCCESS -> {
                    setData(it.data!!)
                    progressBar.visibility(false)
                }
                Status.LOADING -> {
                    progressBar.visibility(true)
                }
            }
        })

        viewModel.requestResults.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> {
                    toast(it.message)
                    progressBar.visibility(false)
                }
                Status.SUCCESS -> {
                    answersMap.forEach { element ->
                        try {
                            ball += element.value.value.toInt()
                        } catch (e: Exception) {
                            //do nothing
                        }
                    }
                    Log.d("ochko", ball.toString())
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra(ResultActivity.RESULT, ball)
                    startActivity(intent)
                    finish()
                    progressBar.visibility(false)
                }
                Status.LOADING -> {
                    progressBar.visibility(true)
                }
            }
        })

        viewModel.getNextQuestions()

        nextButton.setOnClickListener {
            when (questionsCount <= answersMap.size) {
                true -> viewModel.getNextQuestions()
                false -> toast(R.string.please_answer_for_all_questions)
            }
        }

        restartButton.setOnClickListener {
            viewModel.clearData()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }

    override fun onAnswerSelected(questionId: Int, questionName: String, answerText: String, answerValue: String, is_required: Boolean) {
        val answer= AnswerModel(questionName, questionId, answerValue, answerText, is_required)
        answer.id = "answer_$questionId"
        answersMap[answer.id] = answer
    }

    private fun setData(questions: List<QuestionModel>) {
        questionsContainer.removeAllViews()
        questionsCount += questions.filter { it.is_required }.size
        questions.forEach { model ->
            val view = CustomQuestionItem(this, this)
            view.questionNumber.text = String.format("%d.", model.id)
            view.questionText.text = model.question
            view.questionName = model.name
            view.questionId = model.id
            view.setType(model.type)
            view.setReqired(model.is_required)
            view.setVariants(model.answers)
            questionsContainer.addView(view)
        }
        svQuestions.scrollY = 0
    }

    override fun onDestroy() {
        ball = 0
        viewModel.clearData()
        super.onDestroy()
    }


}
