package krazy

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import org.jsoup.Jsoup
import org.jsoup.nodes.Element


object Krazy {

    fun <T: Any> fromUrl(url: String, scope: String, select: (Element) -> T): Result<T, Exception> {
        return Result.of { url.httpGet().responseString() }
                .map { result ->
                    val html = result.third.component1()!!
                    val doc = Jsoup.parse(html)
                    val el = doc.select(scope).first()
                    select(el)
                }
    }


    fun <T: Any> fromUrl(url: String, select: (Element) -> T) = fromUrl(url, "html", select)


    fun <T: Any> fromHtml(html: String, scope: String, select: (Element) -> T): Result<T, Exception> {
        return Result.of {
            val doc = Jsoup.parse(html)
            val el = doc.select(scope).first()
            select(el)
        }
    }

    fun <T: Any> fromHtml(html: String, select: (Element) -> T) = fromHtml(html, "html", select)

}





