package krazy.test

import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import com.winterbe.expekt.expect
import krazy.Krazy
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object _krazySpec : Spek({

    describe("Krazy") {

        it("should be ok") {

            data class BaiduDoc(val title: String)

            val ret = Krazy.fromUrl("http://www.baidu.com") {
                BaiduDoc(title = it.select("title").text())
            }

            ret.success {
                expect(it.title).to.equal("百度一下，你就知道")
            }

            ret.failure { throw it }
        }

        describe("fromHtml") {

            val html = """
              <html>
                <head>
                  <title>Test</title>
                  <meta name="description" content="This is an example html document to test krazy."/>
                </head>
                <body>
                    <article>
                      <h1>The Hobbit</h1>
                      <p>
                        <span>Author:</span>
                        <span>J.R.R. Tolkien</span>
                      </p>
                      <section>
                        <h2>Contents</h2>
                        <ol>
                          <li>Chapter 01: An Unexpected Party</li>
                          <li>Chapter 02: Roast Mutton</li>
                          <li>Chapter 03: A Short Rest</li>
                          <li>Chapter 04: Over Hill and Under Hill</li>
                          <li>Chapter 05: Riddles in the Dark</li>
                          <li>Chapter 06: Out of the Frying-Pan into the Fire</li>
                          <li>Chapter 07: Queer Lodgings</li>
                          <li>Chapter 08: Flies and Spiders</li>
                          <li>Chapter 09: Barrels Out of Bond</li>
                          <li>Chapter 10: A Warm Welcome</li>
                          <li>Chapter 11: On the Doorstep</li>
                          <li>Chapter 12: Inside Information</li>
                          <li>Chapter 13: Not at Home</li>
                          <li>Chapter 14: Fire and Water</li>
                          <li>Chapter 15: The Gathering of the Clouds</li>
                          <li>Chapter 16: A Thief in the Night</li>
                          <li>Chapter 17: The Clouds Burst</li>
                          <li>Chapter 18: The Return Journey</li>
                          <li>Chapter 19: The Last Stage</li>
                        </ol>
                      </section>
                    </article>
                </body>
              </html>
            """

            it("should grab something") {

                data class DocMeta(val title: String, val description: String)

                data class DocModel(
                        val meta: DocMeta,
                        val title: String,
                        val author: String,
                        val contents: List<String>)

                val ret = Krazy.fromHtml(html) {
                    DocModel(
                        meta = DocMeta(
                            it.select("title").text(),
                            it.select("meta[name=description]").attr("content")
                        ),
                        title = it.select("article h1").text(),
                        author = it.select("article p span:eq(1)").text(),
                        contents = it.select("article section ol li").map { it.text() }
                    )
                }

                ret.success {
                    with(it) {
                        with(meta) {
                            expect(title).to.equal("Test")
                            expect(description).to.equal("This is an example html document to test krazy.")
                        }
                        expect(title).to.equal("The Hobbit")
                        expect(author).to.equal("J.R.R. Tolkien")
                        expect(contents).to.have.size(19)
                    }
                }

                ret.failure { throw it }

            }

        }

    }

})