package uz.tuit.oncologic.ui.main.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import uz.tuit.oncologic.data.model.QuestionModel
import uz.tuit.oncologic.ui.custom.CustomQuestionItem

class QuestionListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val view: CustomQuestionItem = itemView as CustomQuestionItem

    fun populateModel(model: QuestionModel) {
        view.questionNumber.text = String.format("%d.", model.id)
        view.questionText.text = model.question
        view.setType(model.type)
        view.setVariants(model.answers)
    }
}