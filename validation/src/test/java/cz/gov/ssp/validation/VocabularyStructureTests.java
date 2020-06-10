package cz.gov.ssp.validation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VocabularyStructureTests {

    private static final Logger log = LoggerFactory.getLogger(VocabularyStructureTests.class);

    private static final String ZSGOV = "z-sgov";
    private static final String VSGOV = "v-sgov";
    private static final String GSGOV = "g-sgov";
    private static final String LSGOV = "l-sgov";
    private static final String ASGOV = "a-sgov";
    private static final String DSGOV = "d-sgov";

    private static Set<String> getSubdirectories(String directory) throws IOException {
        final Path path = Paths.get(directory);
        Set<String> files = Files
            .list(path)
            .filter(Files::isDirectory)
            .map(f -> f.toString())
            .collect(Collectors.toSet());
        return files;
    }

    private static String getRelativeDir(final String sspRootDir) {
        return "../" + sspRootDir;
    }

    private static Set<String> getVocabularyFolders() throws IOException {
        final Set<String> vocabularyFolders = new HashSet<>();
        vocabularyFolders.add(getRelativeDir(ZSGOV));
        vocabularyFolders.add(getRelativeDir(VSGOV));
        vocabularyFolders.addAll(getSubdirectories(getRelativeDir(GSGOV)));
        vocabularyFolders.addAll(getSubdirectories(getRelativeDir(LSGOV)));
        vocabularyFolders.addAll(getSubdirectories(getRelativeDir(ASGOV)));
        vocabularyFolders.addAll(getSubdirectories(getRelativeDir(DSGOV)));
        return vocabularyFolders;
    }

    private static Set<String> getGenericSGoVFolders() {
        final Set<String> set = new HashSet<>();
        set.add(getRelativeDir(GSGOV));
        set.add(getRelativeDir(LSGOV));
        set.add(getRelativeDir(ASGOV));
        set.add(getRelativeDir(DSGOV));
        return set;
    }

    private String getVocabularyName(final String vocabularyFolder) {
        String vocabularyPrefix;
        int index = vocabularyFolder.lastIndexOf(File.separator);
        if (index < 0) {
            vocabularyPrefix = vocabularyFolder;
        } else {
            vocabularyPrefix = vocabularyFolder.substring(index + 1);
        }
        return vocabularyPrefix;
    }

    @ParameterizedTest(name = "Vocabulary folder {0}") @MethodSource("getVocabularyFolders")
    public void testVocabulary(String vocabularyFolder) throws IOException {
        String vocabularyPrefix = getVocabularyName(vocabularyFolder);

        final Pattern regex = Pattern.compile("^" + vocabularyPrefix
            + "-((slovník)|(glosář)|(model)|(mapování)|(diagram))(-[a-zA-Z0-9]+)?[.]ttl");

        AtomicBoolean glosar = new AtomicBoolean(false);
        AtomicBoolean slovnik = new AtomicBoolean(false);
        AtomicBoolean model = new AtomicBoolean(false);

        Files.list(Paths.get(vocabularyFolder)).forEach(file -> {
                if (!Files.isRegularFile(file)) {
                    log.warn("Directory {} not expected inside a vocabulary, ignored", file);
                    return;
                }
                final File f = file.toFile();
                if (!regex.matcher(f.getName()).matches()) {
                    log.warn("Invalid file name {} - not matching {}.",
                        f.getName(), regex.toString());
                    return;
                }
                if (!glosar.get()) {
                    glosar.set(f.getName().startsWith(vocabularyPrefix + "-glosář"));
                }
                if (!model.get()) {
                    model.set(f.getName().startsWith(vocabularyPrefix + "-model"));
                }
                if (!slovnik.get()) {
                    slovnik.set(f.getName().startsWith(vocabularyPrefix + "-slovník"));
                }
            }
        );

        Assertions.assertTrue(slovnik.get() && glosar.get() && model.get());
    }

    @ParameterizedTest(name = "Vocabulary root folder {0}") @MethodSource("getGenericSGoVFolders")
    public void checkGenericSGoVDirectory(String directory) throws IOException {
        final Path path = Paths.get(directory);
        Files
            .list(path)
            .filter(f ->
                !Files.isDirectory(f) || !f.getFileName().startsWith(path.getFileName())
            )
            .forEach(f ->
                log.warn("Unsupported file {}", f.getFileName().toString())
            );
    }
}
