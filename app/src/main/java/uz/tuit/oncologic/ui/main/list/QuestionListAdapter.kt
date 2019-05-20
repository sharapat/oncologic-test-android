//package uz.tuit.oncologic.ui.main.list
//
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import uz.tuit.oncologic.data.model.QuestionModel
//import uz.tuit.oncologic.ui.custom.CustomQuestionItem
//
//class QuestionListAdapter : RecyclerView.Adapter<QuestionListViewHolder>() {
//
//    private var models: List<QuestionModel> = arrayListOf()
//
//    fun setData(models: List<QuestionModel>) {
//        this.models = models
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionListViewHolder {
//        //val view = CustomQuestionItem(parent.context)
////        view.layoutParams = ViewGroup.LayoutParams(
////            ViewGroup.LayoutParams.MATCH_PARENT,
////            ViewGroup.LayoutParams.WRAP_CONTENT
////        )
////        return QuestionListViewHolder(view)
//
//    }
//
//    override fun getItemCount(): Int {
//        return models.size
//    }
//
//    override fun onBindViewHolder(holder: QuestionListViewHolder, position: Int) {
//        holder.populateModel(models[position])
//    }
//}