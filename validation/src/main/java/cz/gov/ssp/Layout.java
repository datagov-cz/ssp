package cz.gov.ssp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Layout {

    public static final String SSP_CONTENT_ROOT = "../obsah/";

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

    private static String getVocabularyDir(final String vocabularyDir) {
        return getRelativeDir("/slovníky/" + vocabularyDir);
    }

    private static String getAssetDir(final String assetDir) {
        return getRelativeDir("/přílohy/" + assetDir);
    }

    public static Set<String> getVocabularyFolders() throws IOException {
        return getSubdirectories(getRelativeDir("/slovníky"));
    }

    public static Set<String> getAssetFolders() throws IOException {
        return getSubdirectories(getRelativeDir("/přílohy"));
    }
}
