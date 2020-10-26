package cz.gov.ssp.validation;

import cz.gov.ssp.Layout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VocabularyStructureTests {

    private static final Logger log = LoggerFactory.getLogger(VocabularyStructureTests.class);

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

    private static Set<String> getVocabularyFolders() throws IOException {
        return Layout.getVocabularyFolders();
    }

    @ParameterizedTest(name = "Vocabulary folder {0}")
    @MethodSource("getVocabularyFolders")
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

    private static Set<String> getGenericSGoVFolders() {
        return Layout.getGenericSGoVFolders();
    }

    @ParameterizedTest(name = "Vocabulary root directory {0}")
    @MethodSource("getGenericSGoVFolders")
    public void checkGenericSGoVDirectory(String vocabularyParent) throws IOException {
        final Path path = Paths.get(vocabularyParent);
        final String vocabularyType = getVocabularyName(vocabularyParent);
        Files
            .list(path)
            .forEach(f -> {
                if (Files.isDirectory(f) && !f.getFileName().toString()
                    .startsWith(vocabularyType)) {
                    log.warn("Wrong directory name {}", f.getFileName().toString());
                } else if (Files.isRegularFile(f)) {
                    log.warn("Unexpected file present in a vocabulary root directory {}",
                        f.getFileName().toString());
                }
            });
    }
}
