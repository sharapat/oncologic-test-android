package uz.tuit.oncologic.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import uz.tuit.oncologic.R
import uz.tuit.oncologic.ui.auth.AuthActivity
import uz.tuit.oncologic.ui.custom.CustomQuestionItem

class MainActivity : AppCompatActivity() {

    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userId = intent.getStringExtra(AuthActivity.USER_ID)

        val view = CustomQuestionItem(this)
        view.questionText.text = "sdasdasdasda"
        view.questionNumber.text = "13"
        container.addView(view)

    }

}
