package uz.tuit.oncologic.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import uz.tuit.oncologic.R
import uz.tuit.oncologic.data.model.QuestionModel
import uz.tuit.oncologic.ui.auth.AuthActivity

class MainActivity : AppCompatActivity() {

    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userId = intent.getStringExtra(AuthActivity.USER_ID)



    }

}
