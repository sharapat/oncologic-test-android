package uz.tuit.oncologic.ui.auth

import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.android.viewmodel.ext.android.viewModel
import uz.pos.botpro.extensions.visibility
import uz.tuit.oncologic.R
import uz.tuit.oncologic.data.model.QuestionModel
import uz.tuit.oncologic.data.model.Resource
import uz.tuit.oncologic.data.model.Status

class AuthActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel.questionList.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> {
                    progressBar?.visibility(true)
                }
                Status.SUCCESS -> {

                }
            }
        })
    }
}
