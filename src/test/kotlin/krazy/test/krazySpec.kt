package krazy.test

import com.winterbe.expekt.expect
import krazy.krazy
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object krazySpec: Spek({

    describe("krazy") {

        it("should be ok") {

            data class MyDoc(val title: String)

            val (doc, err) = krazy("http://www.baidu.com") { el ->
                val title = el.select("title").text()
                MyDoc(title)
            }

            expect(doc?.title).to.equal("百度一下，你就知道")
            println(doc?.title)

        }

    }

})