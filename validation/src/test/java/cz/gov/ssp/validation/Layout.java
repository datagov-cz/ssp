package cz.gov.ssp.validation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

    private static final Pattern getGlossaryRegex(final String vocabularyPrefix) {
        return Pattern.compile("^" + vocabularyPrefix + "-glosář(-[a-zA-Z0-9]+)?[.]ttl$");
    }

    public static Set<File> getGlossaryFiles(final File vocabularyFolder) throws IOException {
        final Pattern pattern = getGlossaryRegex(vocabularyFolder.getName());
        return Files.list(vocabularyFolder.toPath())
            .filter(fileName -> pattern.matcher(fileName.getFileName().toString()).matches())
            .map(file -> file.toFile())
            .collect(Collectors.toSet());
    }
}
