package krazy.test

import com.winterbe.expekt.expect
import krazy.Krazz
import krazy.annotations.Attr
import krazy.annotations.Text
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object krazySpec: Spek({

    describe("Krazz") {

//        it("should be ok") {
//
//            data class MyDoc(val title: String)
//
//            val (doc, err) = Krazz.fromUrl("http://www.baidu.com") { el ->
//                val title = el.select("title").text()
//                MyDoc(title)
//            }
//
//            expect(doc?.title).to.equal("百度一下，你就知道")
//            println(doc?.title)
//
//        }

        describe("fromHtml") {

            val html = """
              <html>
                <head>
                  <title>Test</title>
                  <meta name="docDesc" content="This is an example html document to test krazy."/>
                </head>
                <body>
                    <article>
                      <h1>The Hobbit</h1>
                      <p>
                        <span>Author:</span>
                        <span>J.R.R. Tolkien</span>
                      </p>
                      <p></p>
                    </article>
                </body>
              </html>
            """

            data class DocModel(
                    @Text("title") val docTitle: String,
                    @Attr("meta[name=docDesc]", "content") val docDesc: String,
                    @Text("article h1") val title: String,
                    @Text("article span:eq(1)") val author: String)

            it("should grab content from html") {

                val (ret, err) = Krazz.fromHtml(html, DocModel::class)
                val doc = ret!!

                println(doc)

                with(doc) {
                    expect(docTitle).to.equal("Test")
                    expect(docDesc).to.equal("This is an example html document to test krazy.")
                    expect(title).to.equal("The Hobbit")
                    expect(author).to.equal("J.R.R. Tolkien")
                }

            }



        }

    }

})