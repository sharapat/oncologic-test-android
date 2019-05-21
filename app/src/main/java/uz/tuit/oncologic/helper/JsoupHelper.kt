package uz.tuit.oncologic.helper

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JsoupHelper {
    private lateinit var doc: Document

    fun setHTML(html: String) {
        doc = Jsoup.parse(html)
    }

    fun getResult() : List<String> {
        val elements = doc.getElementsByClass("results__risk h3")
        return when (elements.isNotEmpty()) {
            true -> {
                val result: List<String> = arrayListOf()
                elements.forEach {
                    result.toMutableList().add(it.text())
                }
                return result
            }
            false -> arrayListOf()
        }
    }

    fun getListOfRiskList(): List<List<String>> {
        val elements = doc.getElementsByTag("ul")
        if (elements.isNullOrEmpty()) return arrayListOf()
        val result: List<List<String>> = arrayListOf()
        elements.forEach {
            val listDoc = Jsoup.parse(elements[0].html())
            val list = listDoc.getElementsByTag("li")
            result.toMutableList().add(list.map { it.text() }.toList())
        }
        return result
    }

}