package uz.tuit.oncologic.ui.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import uz.tuit.oncologic.ui.result.list.RiskListAdapter

class ResultActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ResultActivity"
        const val HTML = "html"
    }

    private val adapter1 = RiskListAdapter()
    private val adapter2 = RiskListAdapter()
    private val viewModel: ResultViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        riskList1.adapter = adapter1
        riskList2.adapter = adapter2
        riskList1.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        riskList2.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        riskList1.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        riskList2.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        viewModel.userInfo.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> {
                    personInfo.text = getString(R.string.error)
                    description.visibility = View.GONE
                }
                Status.SUCCESS -> personInfo.text = it.data
                else -> return@Observer
            }
        })
        viewModel.resultTitle1.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> resultTitle1.visibility = View.GONE
                Status.SUCCESS -> {
                    resultTitle1.visibility(true)
                    resultTitle1.text = it.data
                    description.visibility(true)
                }
                else -> return@Observer
            }
        })
        viewModel.resultTitle2.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> resultTitle2.visibility = View.GONE
                Status.SUCCESS -> {
                    resultTitle2.visibility(true)
                    resultTitle2.text = it.data
                }
                else -> return@Observer
            }
        })
        viewModel.riskList1.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> riskList1.visibility = View.GONE
                Status.SUCCESS -> {
                    riskList1.visibility(true)
                    adapter1.setData(it.data!!)
                }
                else -> return@Observer
            }
        })
        viewModel.riskList2.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> riskList2.visibility = View.GONE
                Status.SUCCESS -> {
                    riskList2.visibility(true)
                    adapter2.setData(it.data!!)
                }
                else -> return@Observer
            }
        })
        viewModel.getResults(intent.getStringExtra(HTML))

    }
}
