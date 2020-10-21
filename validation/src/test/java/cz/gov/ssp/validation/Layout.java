package cz.gov.ssp.validation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Layout {
    public static final String ZSGOV = "z-sgov";
    public static final String VSGOV = "v-sgov";
    public static final String GSGOV = "g-sgov";
    public static final String LSGOV = "l-sgov";
    public static final String ASGOV = "a-sgov";
    public static final String DSGOV = "d-sgov";

    public static final String SSP_CONTENT_ROOT = "../content/";

    private static final Pattern getGlossaryRegex(final String vocabularyPrefix) {
        return Pattern.compile("^" + vocabularyPrefix + "-glosář(-[a-zA-Z0-9]+)?[.]ttl$");
    }

    private static Set<String> getSubdirectories(String directory) throws IOException {
        final Path path = Paths.get(directory);
        Set<String> files = Files
            .list(path)
            .filter(Files::isDirectory)
            .map(f -> f.toString())
            .collect(Collectors.toSet());
        return files;
    }

    private static String getRelativeDir(final String sspDir) {
        return SSP_CONTENT_ROOT + sspDir;
    }

    public static Set<String> getVocabularyFolders() throws IOException {
        final Set<String> vocabularyFolders = new HashSet<>();
        vocabularyFolders.add(getRelativeDir(Layout.ZSGOV));
        vocabularyFolders.add(getRelativeDir(Layout.VSGOV));
        vocabularyFolders.addAll(getSubdirectories(getRelativeDir(Layout.GSGOV)));
        vocabularyFolders.addAll(getSubdirectories(getRelativeDir(Layout.LSGOV)));
        vocabularyFolders.addAll(getSubdirectories(getRelativeDir(Layout.ASGOV)));
        vocabularyFolders.addAll(getSubdirectories(getRelativeDir(Layout.DSGOV)));
        return vocabularyFolders;
    }

    public static final Set<String> getGenericSGoVFolders() {
        final Set<String> set = new HashSet<>();
        set.add(getRelativeDir(Layout.GSGOV));
        set.add(getRelativeDir(Layout.LSGOV));
        set.add(getRelativeDir(Layout.ASGOV));
        set.add(getRelativeDir(Layout.DSGOV));
        return set;
    }
}
