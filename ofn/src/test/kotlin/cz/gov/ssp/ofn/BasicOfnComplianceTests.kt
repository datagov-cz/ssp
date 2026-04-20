package cz.gov.ssp.ofn

import Validator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.net.URISyntaxException
import java.util.*

class BasicOfnComplianceTests {
    @ParameterizedTest
    @ValueSource(
        strings = [
            "/cz.gov.ssp.ofn.valid/example-3.2.2.ttl",
            "/cz.gov.ssp.ofn.valid/example-4.2.2.ttl",
            "/cz.gov.ssp.ofn.valid/example-5.2.2.ttl",
            "/cz.gov.ssp.ofn.valid/example-6.2.2.ttl",
            "/cz.gov.ssp.ofn.valid/example-7.2.2.ttl"
        ]
    )
    @Throws(URISyntaxException::class)
    fun testValid(example: String) {
        val report = Validator().validateTurtle(Objects.requireNonNull(javaClass.getResource(example)).toString())
        Assertions.assertTrue(report.conforms())
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "/cz.gov.ssp.ofn.invalid/no-conceptscheme.ttl",
            "/cz.gov.ssp.ofn.invalid/conceptscheme-without-czech-label.ttl",
            "/cz.gov.ssp.ofn.invalid/concept-without-conceptscheme.ttl",
            "/cz.gov.ssp.ofn.invalid/concept-without-czech-label.ttl",
        ]
    )
    fun testInvalid(example: String) {
        val report = Validator().validateTurtle(Objects.requireNonNull(javaClass.getResource(example)).toString())
        Assertions.assertFalse(report.conforms())
    }
}