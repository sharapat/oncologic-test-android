package uz.tuit.oncologic.ui.auth

import android.content.Intent
import androidx.lifecycle.Observer
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.android.viewmodel.ext.android.viewModel
import uz.tuit.oncologic.extensions.visibility
import uz.tuit.oncologic.R
import uz.tuit.oncologic.data.model.Status
import uz.tuit.oncologic.ui.BaseActivity
import uz.tuit.oncologic.ui.main.MainActivity
import java.util.*

class AuthActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    companion object {
        const val TAG = "AuthActivity"
        const val USER_ID = "user_id"
    }

    private var isBirthdateSelected: Boolean = false
    private val viewModel: AuthViewModel by viewModel()
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        tvBirthDate.setOnClickListener {
            showDatePicker()
        }

        viewModel.startTestResult.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> {
                    progressBar?.visibility(true)
                }
                Status.SUCCESS -> {
                    userId = it.data.toString()
                    viewModel.requestTestQuestions(rbMan.isChecked)
                }
                Status.ERROR -> {
                    progressBar?.visibility(false)
                    toast(it.message)
                }
            }
        })

        viewModel.questionList.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> {
                    progressBar?.visibility(true)
                }
                Status.SUCCESS -> {
                    progressBar?.visibility(false)
                    val startTestIntent = Intent(this, MainActivity::class.java)
                    startTestIntent.putExtra(USER_ID, userId)
                    startActivity(startTestIntent)
                    finish()
                }
                Status.ERROR -> {
                    progressBar?.visibility(false)
                    toast(it.message)
                }
            }
        })

        startButton.setOnClickListener {
            when (!TextUtils.isEmpty(etPerson.text)
                    && (rbMan.isChecked || rbWoman.isChecked)
                    && !TextUtils.isEmpty(etLocation.text)
                    && isBirthdateSelected) {

                true -> {
                    val userMap: HashMap<String, Any?> = HashMap()
                    userMap["name"] = etPerson.text.toString()
                    userMap["location"] = etLocation.text.toString()
                    userMap["gender"] = rbMan.isChecked
                    userMap["birthdate"] = tvBirthDate.text.toString()
                    viewModel.saveUser(userMap)
                }
                false -> {
                    toast(R.string.please_fill_the_fields)
                }
            }
        }
    }

    private fun showDatePicker() {
            val now = Calendar.getInstance()
            SpinnerDatePickerDialogBuilder()
                .context(this)
                .callback(this@AuthActivity)
                .spinnerTheme(R.style.NumberPickerStyle)
                .showTitle(true)
                .showDaySpinner(true)
                .defaultDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
                .maxDate(3000, 0, 1)
                .minDate(1996, 11, 26)
                .build()
                .show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        tvBirthDate?.text = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year)
        isBirthdateSelected = true
    }
}
