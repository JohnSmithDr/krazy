package krazy.test

import krazy.annotations.Attr
import krazy.annotations.Scope
import krazy.annotations.Text
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.memberProperties
import kotlin.reflect.primaryConstructor


object reflectSpec: Spek({

    describe("reflect") {

        @Scope("html")
        data class Doc(
                @Text("title") val title: String,
                @Text("author") val author: String)

        it("should be ok") {

            val type = Doc::class

            type.memberProperties.forEach { prop ->
                println("member property: ${prop.name}, accessible: ${prop.isAccessible}")
            }

            type.primaryConstructor?.parameters?.forEach { param ->
                println("parameter: ${param.name}, type: ${param.type}")

                param.annotations.forEach { x ->
                    println(x.annotationClass.simpleName)
                }

            }

        }

        it("can be initialize") {

            val type = Doc::class

            val doc = type.primaryConstructor?.call("The Hobbit", "J. R. R. Tolkien")

            println(doc)

        }
    }

})