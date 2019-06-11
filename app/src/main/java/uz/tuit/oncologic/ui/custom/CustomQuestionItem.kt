package uz.tuit.oncologic.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import uz.tuit.oncologic.R
import uz.tuit.oncologic.data.model.AnswerModel
import uz.tuit.oncologic.data.model.QuestionModel
import uz.tuit.oncologic.ui.main.list.OnAnswerSelectedListener

@SuppressLint("ViewConstructor")
class CustomQuestionItem(private val onAnswerSelectedListener: OnAnswerSelectedListener, context: Context) : CardView(context) {

    private val linearLayoutContainer = LinearLayout(context)
    private val linearLayoutQuestion = LinearLayout(context)
    private val radioGroup: RadioGroup = RadioGroup(context)
    val questionNumber: TextView = TextView(context)
    val questionText: TextView = TextView(context)
    val answersContainerLinearLayout = LinearLayout(context)
    val textInputEditText = EditText(context)
    val radioButtonList: MutableList<RadioButton> = arrayListOf()
    val spinner = Spinner(context)
    private var type: String = ""
    var questionId: Int = 0
    var questionName: String = ""
    var is_required: Boolean = true

    init {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(getPixelSize(8f), getPixelSize(8f), getPixelSize(8f), 0)
        this.layoutParams = layoutParams
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
        elevation = 8f
        this.radius = getPixelSize(4f).toFloat()

        val paddingStandard = getPixelSize(16f)
        linearLayoutContainer.layoutParams = LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        linearLayoutContainer.orientation = LinearLayout.VERTICAL
        linearLayoutContainer.setPadding(paddingStandard, paddingStandard, paddingStandard, paddingStandard)

        addView(linearLayoutContainer)

        linearLayoutQuestion.layoutParams = LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        linearLayoutQuestion.orientation = LinearLayout.HORIZONTAL

        questionNumber.layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        questionNumber.setPadding(0, 0, getPixelSize(16f), 0)
        questionNumber.setTextColor(ContextCompat.getColor(context, R.color.blackish))
        questionNumber.textSize = 18f
        linearLayoutQuestion.addView(questionNumber)

        questionText.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        questionText.setTextColor(ContextCompat.getColor(context, R.color.blackish))
        questionText.textSize = 18f
        linearLayoutQuestion.addView(questionText)

        linearLayoutContainer.addView(linearLayoutQuestion)

        answersContainerLinearLayout.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        answersContainerLinearLayout.orientation = LinearLayout.VERTICAL
        linearLayoutContainer.addView(answersContainerLinearLayout)

        //for text type
        textInputEditText.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        textInputEditText.textSize = 18f
        textInputEditText.setTextColor(ContextCompat.getColor(context, R.color.blackish))
        textInputEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onAnswerSelectedListener.onAnswerSelected(questionId, questionName, s.toString(), s.toString(), is_required)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        //for radio type
        answersContainerLinearLayout.setPadding(0, 0, 0, 0)
        radioGroup.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        radioGroup.orientation = RadioGroup.VERTICAL

        spinner.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        spinner.background = ContextCompat.getDrawable(context, R.drawable.spinner_background)
    }

    fun setVariants(variants: Map<String, String>) {
        radioButtonList.clear()
        when (variants.size >= 5) {
            true -> {
                val list: MutableList<AnswerModel> = arrayListOf()
                val choose = AnswerModel()
                choose.text = "Быберите"
                list.add(choose)
                val sortedVariants = variants.toList().sortedBy { (key, _)->
                    key
                }.toMap()
                sortedVariants.forEach {
                    val model = AnswerModel()
                    model.text = it.key
                    model.value = it.value
                    list.add(model)
                }
                spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position != 0) {
                            val answer = (spinner.selectedItem as AnswerModel)
                            onAnswerSelectedListener.onAnswerSelected(questionId, questionName, answer.text, answer.value, is_required)
                        }
                    }

                }
                spinner.adapter = getSpinnerAdapter(list)
                spinner.setSelection(0)
                answersContainerLinearLayout.setPadding(0, getPixelSize(16f), 0, 0)
                answersContainerLinearLayout.addView(spinner)
            }
            false -> {
                variants.forEach {
                    val radioButton = RadioButton(context)
                    radioButton.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    radioButton.setTextColor(ContextCompat.getColor(context, R.color.blackish))
                    radioButton.textSize = 18f
                    radioButton.tag = it
                    radioButton.text = it.key
                    radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            val answer= (radioButton.tag as Map.Entry<String, String>)
                            onAnswerSelectedListener.onAnswerSelected(questionId, questionName, answer.key, answer.value, is_required)
                        }
                    }
                    radioButtonList.add(radioButton)
                    radioGroup.addView(radioButton)
                }
            }
        }
    }

    fun setReqired(is_required: Boolean) {
        this.is_required = is_required
    }

    fun setType(type: String) {
        this.type = type
        answersContainerLinearLayout.removeAllViews()
        radioGroup.removeAllViews()
        when (type) {
            QuestionModel.TYPE_TEXT -> {
                answersContainerLinearLayout.addView(textInputEditText)
            }
            QuestionModel.TYPE_RADIO -> {
                answersContainerLinearLayout.addView(radioGroup)
            }
        }
    }

    private fun getPixelSize(dp: Float) : Int {
        val r = resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            r.displayMetrics
        ).toInt()
    }

    private fun getSpinnerAdapter(list: List<Any>): ArrayAdapter<Any> {
        val spinnerAdapter: ArrayAdapter<Any> = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, list)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return spinnerAdapter
    }
}