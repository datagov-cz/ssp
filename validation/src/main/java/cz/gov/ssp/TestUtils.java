package cz.gov.ssp;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {

    public static String getVocabularyBase(final String g) {
        final List<VocabularyFile>
            artifacts = Arrays
            .stream(VocabularyFile.values())
            .filter(v -> !VocabularyFile.slovnik.equals(v)).collect(
            Collectors.toList());

        for (VocabularyFile va : artifacts) {
            if (g.contains(va.getLocalName())) {
                return g.substring(0, g.lastIndexOf("/" + va.getLocalName()));
            }
        }
        return g;
    }
}
