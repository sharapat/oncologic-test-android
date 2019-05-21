package uz.tuit.oncologic.ui.result.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.tuit.oncologic.R

class RiskListAdapter : RecyclerView.Adapter<RiskViewHolder>() {

    private var models: List<String> = arrayListOf()

    fun setData(models: List<String>) {
        this.models = models
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_risk, parent, false)
        return RiskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onBindViewHolder(holder: RiskViewHolder, position: Int) {
        holder.populateModel(models[position])
    }
}