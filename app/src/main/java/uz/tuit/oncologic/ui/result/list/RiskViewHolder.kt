package uz.tuit.oncologic.ui.result.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.tuit.oncologic.R

class RiskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val text: TextView = itemView.findViewById(R.id.text)

    fun populateModel(text: String) {
        this.text.text = text
    }
}