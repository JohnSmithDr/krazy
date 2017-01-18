package krazy

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import org.jsoup.Jsoup
import org.jsoup.nodes.Element


fun <T: Any> krazy(url: String, scope: String, select: (Element) -> T): Result<T, Exception> {

    val req = { url.httpGet().responseString() }

    return Result.of(req)
            .map { result ->
                val html = result.third.component1()!!
                val doc = Jsoup.parse(html)
                val el = doc.select(scope).first()
                select(el)
            }

}

fun <T: Any> krazy(url: String, select: (Element) -> T) = krazy.krazy(url, "html", select)
