package uz.tuit.oncologic.data.model

data class QuestionModel (
    var question: String = "",
    var id: Int = 0,
    var name: String = "",
    var page: Int = 0,
    var type: String = "",
    var answers: Map<String, String> = HashMap(),
    var is_required: Boolean = true
) {
    companion object {
        const val TYPE_RADIO = "radio"
        const val TYPE_TEXT = "text"
    }
}