package cz.gov.ssp.validation;


import static cz.gov.ssp.OntologyUtils.addAttachmentToModel;
import static org.junit.jupiter.api.Assertions.fail;

import cz.gov.ssp.AttachmentFile;
import cz.gov.ssp.Constants;
import cz.gov.ssp.Layout;
import cz.gov.ssp.OntologyUtils;
import cz.gov.ssp.VocabularyFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AttachmentsConsistencyTests {

    /**
     *   If true tests might modify underlying data to fix found issues. This property
     *   can be overridden by environment variable passed to the test.
     */
    private static boolean isFixErrors = false;
    private static final Logger log = LoggerFactory.getLogger(AttachmentsConsistencyTests.class);

    @BeforeAll
    public static void init() {
        isFixErrors = Optional.ofNullable(System.getenv("FIX_ERRORS")).
            map(v -> v.equalsIgnoreCase("true")).orElse(isFixErrors);
    }

    @Test
    void vocabulariesReferenceOnlyExistingAttachments() throws IOException {

        Dataset attachmentsDataset = DatasetFactory.create();
        Layout.getAttachmentFolders().forEach(folder -> {
            try {
                addAttachmentToModel(attachmentsDataset, folder, AttachmentFile.příloha);
            } catch (Exception e) {
                log.error("Error during processing of " + folder, e);
                fail();
            }
        });
        boolean foundError = false;
        for (String vocabDir : Layout.getVocabularyFolders()) {
            final Model model = OntologyUtils.createModel(vocabDir, VocabularyFile.přílohy.name());
            log.trace("Processing vocabulary from directory " + vocabDir + ".");
            List<Statement> stList =
                model.listStatements(null, Constants.MA_PRILOHU, (RDFNode) null).
                    filterDrop(
                        st ->
                            !attachmentsDataset.getNamedModel(
                                st.getObject().asResource().getURI()
                            ).isEmpty()
                    ).toList();
            if (!stList.isEmpty()) {

                foundError = true;
                logError(
                    "Found reference from {} to invalid attachments: {}",
                    vocabDir,
                    stList.stream().map(Statement::getObject).collect(Collectors.toSet())
                );

                // apply fix
                if (isFixErrors) {
                    model.remove(stList);
                    savePrilohyModel(vocabDir, model);
                }
            }
        }
        if (foundError && (!isFixErrors)) {
            fail("Found invalid references to attachments, see errors above.");
        }
    }


    @Test
    void attachmentFilesAreNotEmpty() throws IOException {
        Set<Path> emptyAttachments = getEmptyAttachments();

        boolean emptyAttachmentsExists = !emptyAttachments.isEmpty();

        if (emptyAttachmentsExists) {
            logError("Found empty attachments: {}", emptyAttachments);

            if (!isFixErrors) {
                Assertions.fail();
            }

            // apply fix
            log.info("Removing found empty attachments ...");
            emptyAttachments.forEach(
                this::deleteIncludingEmptyParentFolder
            );
        }
    }

    @Test
    void attachmentReferenceFilesAreNotEmpty() throws IOException {
        boolean foundError = false;
        for (String vocabDir : Layout.getVocabularyFolders()) {
            final File[] prilohyFiles = OntologyUtils.getFiles(vocabDir, VocabularyFile.přílohy.name());
            log.trace("Processing vocabulary from directory " + vocabDir + ".");

            for (File prilohyFile : prilohyFiles) {
                if (isEmpty(prilohyFile.toPath())) {
                    foundError = true;
                    logError(
                        "Found empty attachment reference file {}.",
                        prilohyFile
                    );
                    if (isFixErrors) {
                        log.info("Deleting empty attachment reference file {}.", prilohyFile);
                        prilohyFile.delete();
                    }
                }
            }
        }
        if (foundError && (!isFixErrors)) {
            fail("Found empty attachment reference files, see errors above.");
        }
    }

    /**
     * List or empty attachment file paths.
     *
     * @return list of all attachments that are empty.
     */
    Set<Path> getEmptyAttachments() throws IOException {
        Set<Path> emptyAttachments = new HashSet<>();
        for (String af : Layout.getAttachmentFolders()) {
            final Path path = Paths.get(af);
            emptyAttachments.addAll(
                Files.list(path)
                    .filter(this::isEmpty)
                    .collect(Collectors.toSet())
            );
        }
        return emptyAttachments;
    }

    /**
     * Delete file on specified file and delete its parent if it becomes after deletion empty.
     *
     * @param file File to delete.
     */
    void deleteIncludingEmptyParentFolder(Path file) {
        Path parentFolder = file.getParent();
        try {
            Files.delete(file);
            if (isEmpty(parentFolder)) {
                Files.delete(parentFolder);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean isEmpty(Path path) {
        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                return !entries.findFirst().isPresent();
            } catch (IOException e) {
                throw new IllegalArgumentException(
                    "Could not list file in directory " + path + "."
                );
            }
        }

        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException(
                "Path " + path + " does not reference to a a regular file."
            );
        }
        try {
            return Files.size(path) == 0;
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "Could find size of the file " + path,
                e
            );
        }
    }

    private void savePrilohyModel(String vocabDir, Model model) throws IOException {
        File prilohyFile =
            Arrays.stream(
                    OntologyUtils.getFiles(vocabDir, VocabularyFile.přílohy.name()))
                .findFirst().orElseThrow(
                    () -> new IllegalStateException("Could not access vocabulary artifact "
                        + VocabularyFile.přílohy.name() + " within " + vocabDir)
                );
        model.write(Files.newOutputStream(prilohyFile.toPath()), FileUtils.langTurtle);
    }

    /**
     * Log error with appropriate log level. Method logs using warn level instead of level error
     * in case automatic fixes of errors are enabled.
     *
     * @param message Message to log.
     * @param arguments Arguments substituted to the message.
     */
    private void logError(String message, Object... arguments) {
        if (isFixErrors) {
            log.warn(message, arguments);
        } else {
            log.error(message, arguments);
        }
    }
}
