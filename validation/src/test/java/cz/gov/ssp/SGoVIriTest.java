package cz.gov.ssp;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SGoVIriTest {

    @ParameterizedTest
    @MethodSource("getVocabularyIris")
    public void testParsesValidSGoVIris(final String iri) {
        new SGoVIri(iri);
    }

    public static Stream<String> getVocabularyIris() {
        return Stream.of(
            "https://slovník.gov.cz/základní",
            "https://slovník.gov.cz/veřejný-sektor",
            "https://slovník.gov.cz/generický/číselníky",
            "https://slovník.gov.cz/agendový/a101",
            "https://slovník.gov.cz/datový/číselníky",
            "https://slovník.gov.cz/legislativní/sbírka/183/2006"
        );
    }

    @ParameterizedTest
    @MethodSource("getVocabularyIdPairs")
    public void testExtractsVocabularyIdCorrectly(final String iri,
                                                  final String expectedVocabularyId) {
        Assertions.assertEquals(expectedVocabularyId, new SGoVIri(iri).extractVocabularyId());
    }

    public static Stream<Arguments> getVocabularyIdPairs() {
        return Stream.of(
            Arguments.of("https://slovník.gov.cz/základní", "základní"),
            Arguments.of("https://slovník.gov.cz/veřejný-sektor", "veřejný-sektor"),
            Arguments.of("https://slovník.gov.cz/agendový/a101", "agendový/a101"),
            Arguments.of("https://slovník.gov.cz/legislativní/sbírka/183/2006",
                "legislativní/sbírka/183/2006")
        );
    }

    @ParameterizedTest
    @MethodSource("getInvalidVocabularyIdPairs")
    public void testNotParsesInvalidIri(final String iri) {
        Assertions.assertThrows(RuntimeException.class, () -> {
            new SGoVIri(iri).extractVocabularyId();
        });
    }

    public static Stream<Arguments> getInvalidVocabularyIdPairs() {
        return Stream.of(
            Arguments.of("https://slovníkx.gov.cz/veřejný-sektor", "veřejný-sektor")
        );
    }

    @ParameterizedTest
    @MethodSource("getVocabularyComponentsPairs")
    public void testExtractsVocabularyComponentsCorrectly(final String iri,
                                                          final String expectedVocabularyId,
                                                          final VocabularyFile expectedVocabularyArtifact) {
        Assertions.assertEquals(expectedVocabularyArtifact,
            new SGoVIri(iri).extractVocabularyArtifact());
        Assertions.assertEquals(expectedVocabularyId, new SGoVIri(iri).extractVocabularyId());
    }

    public static Stream<Arguments> getVocabularyComponentsPairs() {
        return Stream.of(
            Arguments.of("https://slovník.gov.cz/základní", "základní", VocabularyFile.slovník),
            Arguments.of("https://slovník.gov.cz/veřejný-sektor", "veřejný-sektor",
                VocabularyFile.slovník),
            Arguments.of("https://slovník.gov.cz/agendový/a101", "agendový/a101",
                VocabularyFile.slovník),
            Arguments.of("https://slovník.gov.cz/legislativní/sbírka/183/2006",
                "legislativní/sbírka/183/2006", VocabularyFile.slovník),
            Arguments.of("https://slovník.gov.cz/legislativní/sbírka/183/2006/glosář",
                "legislativní/sbírka/183/2006", VocabularyFile.glosář),
            Arguments.of("https://slovník.gov.cz/legislativní/sbírka/183/2006/model",
                "legislativní/sbírka/183/2006", VocabularyFile.model),
            Arguments.of("https://slovník.gov.cz/legislativní/sbírka/183/2006/mapování",
                "legislativní/sbírka/183/2006", VocabularyFile.mapování),
            Arguments.of("https://slovník.gov.cz/legislativní/sbírka/183/2006/přílohy",
                "legislativní/sbírka/183/2006", VocabularyFile.přílohy)
        );
    }

    @ParameterizedTest
    @MethodSource("getVocabularyBasePairs")
    public void testGetVocabularyBaseGetsItCorrectly(final String base, final String artifact) {
        Assertions.assertEquals(base, new SGoVIri(artifact).extractBase() );
    }

    public static Stream<Arguments> getVocabularyBasePairs() {
        return Stream.of(
            Arguments.of("https://slovník.gov.cz/základní", "https://slovník.gov.cz/základní/glosář"),
            Arguments.of("https://slovník.gov.cz/veřejný-sektor", "https://slovník.gov.cz/veřejný-sektor/model"),
            Arguments.of("https://slovník.gov.cz/veřejný-sektor", "https://slovník.gov.cz/veřejný-sektor/mapování"),
            Arguments.of("https://slovník.gov.cz/veřejný-sektor", "https://slovník.gov.cz/veřejný-sektor/mapování/eli")
        );
    }
}
