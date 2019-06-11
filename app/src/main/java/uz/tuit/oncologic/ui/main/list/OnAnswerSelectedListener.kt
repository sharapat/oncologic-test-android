package uz.tuit.oncologic.ui.main.list

interface OnAnswerSelectedListener {
    fun onAnswerSelected(questionId: Int, questionName: String, answerText: String, answerValue: String, is_required: Boolean)
}