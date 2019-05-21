package uz.tuit.oncologic.ui.result

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.tuit.oncologic.data.AppRepository
import uz.tuit.oncologic.data.model.Resource
import uz.tuit.oncologic.helper.JsoupHelper

class ResultViewModel(private val repository: AppRepository, private val jsoupHelper: JsoupHelper) : ViewModel() {
    val userInfo = MutableLiveData<Resource<String>>()
    val resultTitle1 = MutableLiveData<Resource<String>>()
    val resultTitle2 = MutableLiveData<Resource<String>>()
    val riskList1 = MutableLiveData<Resource<List<String>>>()
    val riskList2 = MutableLiveData<Resource<List<String>>>()

    fun getResults(html: String) {
        jsoupHelper.setHTML(html)
        val resultList = jsoupHelper.getResult()
        val riskList = jsoupHelper.getListOfRiskList()

        when (repository.getUsername() == null) {
            true -> userInfo.value = Resource.error("unknown username")
            false -> userInfo.value = Resource.success(repository.getUsername())
        }
        when (resultList.size) {
            0 -> {
                resultTitle1.value = Resource.error("there is no result")
                resultTitle2.value = Resource.error("there is no result")
            }
            1 -> {
                resultTitle1.value = Resource.success(resultList[0])
                resultTitle2.value = Resource.error("there is no result")
            }
            2 -> {
                resultTitle1.value = Resource.success(resultList[0])
                resultTitle2.value = Resource.success(resultList[1])
            }
        }
        when (riskList.size) {
            0 -> {
                riskList1.value = Resource.error("there is no result")
                riskList2.value = Resource.error("there is no result")
            }
            1 -> {
                riskList1.value = Resource.success(riskList[0])
                riskList2.value = Resource.error("there is no result")
            }
            2 -> {
                riskList1.value = Resource.success(riskList[0])
                riskList2.value = Resource.success(riskList[1])
            }
        }
    }
}