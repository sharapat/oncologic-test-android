package uz.tuit.oncologic.data.model

import com.google.firebase.Timestamp

data class UserModel (
    var birthdate: Timestamp = Timestamp.now(),
    var id: String = "",
    var name: String = "",
    var location: String = "",
    var gender: Boolean = true,
    var answers: List<String> = arrayListOf()
)