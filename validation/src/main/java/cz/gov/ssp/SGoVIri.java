package cz.gov.ssp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SGoVIri {

    private final static String BASE = "https://slovník.gov.cz/";

    private final static Pattern regexMain =
        Pattern.compile("^" + BASE + "(.+)/(glosář|model|mapování(/.*)?|přílohy)$");

    private final static Pattern regexVocabulary = Pattern.compile("^" + BASE + "(.+)$");

    private final Matcher m;

    public SGoVIri(final String originalIri) {
        Matcher matcher = regexMain.matcher(originalIri);
        if (!matcher.matches()) {
            matcher = regexVocabulary.matcher(originalIri);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Invalid SGoV IRI " + originalIri);
            }
        }
        m = matcher;
    }

    public String extractBase() {
        return BASE + m.group(1);
    }

    public String extractVocabularyId() {
        return m.groupCount() < 1 ? null : m.group(1);
    }

    public VocabularyFile extractVocabularyArtifact() {
        final String s = m.groupCount() < 2 ? null : m.group(2);
        return s == null ? VocabularyFile.slovník : VocabularyFile.valueOf(s);
    }
}
