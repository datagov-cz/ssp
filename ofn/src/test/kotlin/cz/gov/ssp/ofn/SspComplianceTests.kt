package cz.gov.ssp.ofn

import Validator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File
import java.net.URISyntaxException
import java.util.*

class SspComplianceTests {
    @ParameterizedTest
    @ValueSource(
        strings = [
            "../content/vocabularies/v-sgov/v-sgov-glosář-ofn.ttl",
        ]
    )
    @Throws(URISyntaxException::class)
    fun testValid(example: String) {
        val report = Validator().validateTurtle(File(example).absolutePath)
        Assertions.assertTrue(report.conforms())
    }
}