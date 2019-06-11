package uz.tuit.oncologic.data.model

import com.google.gson.annotations.SerializedName

data class AnswerModel (

    @SerializedName("question_name")
    var questionName: String = "",

    @SerializedName("question_id")
    var questionId: Int = 0,

    var value: String = "",
    var text: String = "",
    var is_required: Boolean = true,
    var id: String = ""

) {
    override fun toString(): String {
        return text
    }
}