package uz.tuit.oncologic.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
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

class MainActivity : BaseActivity(), OnAnswerSelectedListener {

    private val viewModel: MainViewModel by viewModel()
    private var questionsCount = 0
    private var userId: String = ""
    private val answersMap: HashMap<String, AnswerModel> = HashMap()
    private var response = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userId = intent.getStringExtra(AuthActivity.USER_ID)

        viewModel.questions.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> {
                    if (it.message == "End of questions") {
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra(ResultActivity.HTML, response)
                        startActivity(intent)
                    } else {
                        toast(it.message)
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
                    response = it.data!!
                    viewModel.getNextQuestions()
                    progressBar.visibility(false)
                }
                Status.LOADING -> {
                    progressBar.visibility(true)
                }
            }
        })

        viewModel.getNextQuestions()

        nextButton.setOnClickListener {
            when (questionsCount == answersMap.size) {
                true -> viewModel.sendResults(userId, answersMap)
                false -> toast(R.string.please_answer_for_all_questions)
            }
        }

        restartButton.setOnClickListener {
            viewModel.restart()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

    }

    override fun onAnswerSelected(questionId: Int, questionName: String, answerText: String, answerValue: String) {
        val answer= AnswerModel(questionName, questionId, answerValue, answerText)
        answer.id = "answer_$questionId"
        answersMap[answer.id] = answer
    }

    private fun setData(questions: List<QuestionModel>) {
        questionsContainer.removeAllViews()
        questionsCount += questions.size
        questions.forEach { model ->
            val view = CustomQuestionItem(this, this)
            view.questionNumber.text = String.format("%d.", model.id)
            view.questionText.text = model.question
            view.questionName = model.name
            view.questionId = model.id
            view.setType(model.type)
            view.setVariants(model.answers)
            questionsContainer.addView(view)
        }
        svQuestions.scrollY = 0
    }
}
