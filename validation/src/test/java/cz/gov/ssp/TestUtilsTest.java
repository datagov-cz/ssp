package cz.gov.ssp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestUtilsTest {

    @Test
    public void testGetVocabularyBaseGetsItCorrectly() {
        Assertions.assertEquals("https://slovník.gov.cz/základní", TestUtils.getVocabularyBase("https://slovník.gov.cz/základní/glosář") );
        Assertions.assertEquals("https://slovník.gov.cz/veřejný-sektor", TestUtils.getVocabularyBase("https://slovník.gov.cz/veřejný-sektor/model") );
        Assertions.assertEquals("https://slovník.gov.cz/veřejný-sektor", TestUtils.getVocabularyBase("https://slovník.gov.cz/veřejný-sektor/mapování") );
        Assertions.assertEquals("https://slovník.gov.cz/veřejný-sektor", TestUtils.getVocabularyBase("https://slovník.gov.cz/veřejný-sektor/mapování/eli") );
    }
}
