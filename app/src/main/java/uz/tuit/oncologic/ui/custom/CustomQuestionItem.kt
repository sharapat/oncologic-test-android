package uz.tuit.oncologic.ui.custom

import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import uz.tuit.oncologic.R
import uz.tuit.oncologic.data.model.QuestionModel

class CustomQuestionItem(context: Context) : CardView(context) {

    private val linearLayoutContainer = LinearLayout(context)
    private val linearLayoutQuestion = LinearLayout(context)
    private val radioGroup: RadioGroup = RadioGroup(context)
    val questionNumber: TextView = TextView(context)
    val questionText: TextView = TextView(context)
    val answersContainerLinearLayout = LinearLayout(context)
    val textInputEditText = EditText(context)
    val radioButtonList: MutableList<RadioButton> = arrayListOf()
    private var type: String = ""

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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
        questionNumber.textSize = 16f
        linearLayoutQuestion.addView(questionNumber)

        questionText.layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        questionText.setTextColor(ContextCompat.getColor(context, R.color.blackish))
        questionText.textSize = 16f
        linearLayoutQuestion.addView(questionText)

        linearLayoutContainer.addView(linearLayoutQuestion)

        answersContainerLinearLayout.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        answersContainerLinearLayout.setPadding(0, getPixelSize(16f), 0, 0)
        answersContainerLinearLayout.orientation = LinearLayout.VERTICAL
        linearLayoutContainer.addView(answersContainerLinearLayout)

        //for text type
        textInputEditText.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        textInputEditText.textSize = 16f
        textInputEditText.setTextColor(ContextCompat.getColor(context, R.color.blackish))

        //for radio type
        answersContainerLinearLayout.setPadding(0, 0, 0, 0)
        radioGroup.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

    fun setType(type: String) {
        this.type = type
        when (type) {
            QuestionModel.TYPE_TEXT -> answersContainerLinearLayout.addView(textInputEditText)

        }
    }

    fun getType() : String = type

    private fun getPixelSize(dp: Float) : Int {
        val r = resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            r.displayMetrics
        ).toInt()
    }

}