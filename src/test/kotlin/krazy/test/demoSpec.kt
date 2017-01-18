import com.winterbe.expekt.expect
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object demoSpec : Spek({

    describe("test") {

        it("should be ok") {
            expect("OK").to.equal("OK")
            expect("OK").to.not.equal("ok")
        }

    }

})