package cz.gov.ssp.validation;

import static cz.github.sgov.server.Validator.getVocabularyRules;


import cz.github.sgov.server.Validator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.util.SHACLPreferences;
import org.topbraid.shacl.validation.ValidationReport;
import org.topbraid.shacl.vocabulary.SH;

public class SGoVTests {

    private static final Logger log = LoggerFactory.getLogger(SGoVTests.class);

    @BeforeEach
    public void init() {
        SHACLPreferences.setProduceFailuresMode(true);
    }

    private static Stream<String> getGlossaries() throws IOException {
        return Files.walk(Paths.get("../")).filter(Files::isRegularFile).map(Path::toFile)
            .filter(f -> f.getName().contains("glosář")).map(File::getAbsolutePath);
    }

    @ParameterizedTest(name = "Testing glossary {0}") @MethodSource("getGlossaries")
    public void testGlossary(String glossaryFile) {
        final Validator validator = new Validator();

        final Model dataModel =
            JenaUtil.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, null);

        OntDocumentManager.getInstance().setProcessImports(false);
        dataModel.read(glossaryFile, null, FileUtils.langTurtle);

        final ValidationReport r = validator.validate(dataModel, Validator.getGlossaryRules());

        r.results().forEach(result -> log.info(MessageFormat
            .format("[{0}] Node {1} failing for value {2} with message: {3} ",
                result.getSeverity().getLocalName(), result.getFocusNode(), result.getValue(),
                result.getMessage())));

        Assertions.assertTrue(r.results().stream().allMatch(
            res -> res.getSeverity().equals(SH.Warning) || res.getSeverity().equals(SH.Info)));
    }

    private static Set<String> getVocabularies() {
        final Set<String> vocabularies = new HashSet<>();
        vocabularies.add(Layout.ZSGOV);
        vocabularies.add(Layout.VSGOV);
        return vocabularies;
    }

    @ParameterizedTest(name = "Testing vocabulary content {0}") @MethodSource("getVocabularies")
    public void testVocabularyContent(String vocabularyFolder) throws IOException {
        final Set<String> rules = getVocabularyRules();
        String root = "../";
        Stream<String> files =
            Files.walk(Paths.get(root)).filter(Files::isRegularFile).map(Path::toFile)
                .filter(f -> f.getPath().startsWith(root + vocabularyFolder))
                .filter(f -> f.getName().endsWith(".ttl")).map(File::getAbsolutePath);

        final Validator validator = new Validator();

        final Model dataModel =
            JenaUtil.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, null);

        OntDocumentManager.getInstance().setProcessImports(false);
        files.forEach(f -> {
            log.info("Loading " + f);
            dataModel.read(f, null, FileUtils.langTurtle);
        });

        final ValidationReport r = validator.validate(dataModel, rules);

        r.results().forEach(result -> log.info(MessageFormat
            .format("[{0}] Node {1} failing for value {2} with message: {3} ",
                result.getSeverity().getLocalName(), result.getFocusNode(), result.getValue(),
                result.getMessage())));

        Assertions.assertTrue(r.results().stream().allMatch(
            res -> res.getSeverity().equals(SH.Warning) || res.getSeverity().equals(SH.Info)));
    }
}
