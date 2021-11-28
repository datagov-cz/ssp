package cz.gov.ssp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class Layout {

    private static Set<String> getSubdirectories(String directory) throws IOException {
        final Path path = Paths.get(directory);
        if (Files.exists(path)) {
            return
                Files
                    .list(path)
                    .filter(Files::isDirectory)
                    .map(f -> f.toString())
                    .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }

    public static Set<String> getVocabularyFolders() throws IOException {
        return getSubdirectories("../content/vocabularies");
    }

    public static Set<String> getAttachmentFolders() throws IOException {
        return getSubdirectories("../content/attachments");
    }
}
