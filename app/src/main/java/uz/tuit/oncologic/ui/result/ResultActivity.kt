package uz.tuit.oncologic.ui.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_result.*
import org.koin.android.viewmodel.ext.android.viewModel
import uz.tuit.oncologic.R
import uz.tuit.oncologic.data.model.Status
import uz.tuit.oncologic.extensions.visibility
import uz.tuit.oncologic.ui.auth.AuthActivity
import uz.tuit.oncologic.ui.result.list.RiskListAdapter

class ResultActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ResultActivity"
        const val RESULT = "result"
        const val TOTAL_MAN = 984
        const val TOTAL_WOMAN = 990
    }

    private val viewModel: ResultViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        personInfo.text = intent.getIntExtra(RESULT, 0).toString()

        viewModel.userInfo.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> {
                    personInfo.text = getString(R.string.error)
                    description.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    personInfo.text = it.data?.name
                    tvResult.text = String.format("Риск появление онкологических заболевании в вашем организме %.2f %s",
                        (100.0 * intent.getIntExtra(RESULT, 0) / when(it.data?.gender) {
                            false -> TOTAL_WOMAN
                            else -> TOTAL_MAN
                        }), "%"
                    )
                }
                else -> return@Observer
            }
        })

        viewModel.getResults(intent.getIntExtra(RESULT, 0))

        restartButton.setOnClickListener {
            viewModel.clearData()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}
