package krazy

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import krazy.annotations.Attr
import krazy.annotations.Text
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.*
import kotlin.reflect.*
import kotlin.reflect.jvm.isAccessible


object Krazz {

    fun <T: Any> fromUrl(url: String, scope: String, select: (Element) -> T): Result<T, Exception> {
        return Result.of { url.httpGet().responseString() }
                .map { result ->
                    val html = result.third.component1()!!
                    val doc = Jsoup.parse(html)
                    val el = doc.select(scope).first()
                    select(el)
                }
    }

    fun <T: Any> fromUrl(url: String, scope: String, type: KClass<T>): Result<T, Exception> = fromUrl(url, scope, selectorOf(type))

    fun <T: Any> fromUrl(url: String, select: (Element) -> T) = fromUrl(url, "html", select)

    fun <T: Any> fromUrl(url: String, type: KClass<T>) = fromUrl(url, "html", selectorOf(type))

    fun <T: Any> fromHtml(html: String, scope: String, select: (Element) -> T): Result<T, Exception> {
        return Result.of {
            val doc = Jsoup.parse(html)
            val el = doc.select(scope).first()
            select(el)
        }
    }

    fun <T: Any> fromHtml(html: String, select: (Element) -> T) = fromHtml(html, "html", select)

    fun <T: Any> fromHtml(html: String, scope: String, type: KClass<T>): Result<T, Exception> = fromHtml(html, scope, selectorOf(type))

    fun <T: Any> fromHtml(html: String, type: KClass<T>): Result<T, Exception> = fromHtml(html, "html", selectorOf(type))

    private fun <T: Any> selectorOf(type: KClass<T>): (Element) -> T {
        return fun(element: Element): T = initByElement(type, element)
    }

    private fun <T: Any> initByElement(type: KClass<T>, element: Element): T {
        val ctor = type.primaryConstructor!!
        val args = HashMap<KParameter, Any>()

        ctor.parameters.forEach { param ->

            val annotation = param.annotations.lastOrNull { (it is Text) or (it is Attr) }

            when(annotation) {
                is Text -> {
                    args.put(param, element.select(annotation.selector).text())
                }
                is Attr -> {
                    args.put(param, element.select(annotation.selector).attr(annotation.attr))
                }
                else -> {
                    args.put(param, Unit)
                }
            }
        }

        return ctor.callBy(args)
    }

}





