package cz.gov.ssp;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {

    public static String getVocabularyBase(String g) {
        final List<VocabularyArtifact>
            artifacts = Arrays
            .stream(VocabularyArtifact.values())
            .filter(v -> !VocabularyArtifact.vocabulary.equals(v)).collect(
            Collectors.toList());

        for (VocabularyArtifact va : artifacts) {
            if (g.contains(va.getArtifactIriLocalName())) {
                return g.substring(0, g.lastIndexOf("/" + va.getArtifactIriLocalName()));
            }
        }
        return g;
    }
}
