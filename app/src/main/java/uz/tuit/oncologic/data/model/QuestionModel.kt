package uz.tuit.oncologic.data.model

data class QuestionModel (
    var question: String = "",
    var id: Int = 0,
    var name: String = "",
    var page: Int = 0,
    var type: String = "",
    var answers: Map<String, String> = HashMap()
)