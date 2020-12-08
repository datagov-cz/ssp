package cz.gov.ssp.validation;

import com.github.sgov.server.Validator;
import cz.gov.ssp.Layout;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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
import org.topbraid.shacl.validation.ValidationResult;
import org.topbraid.shacl.vocabulary.SH;

public class ConsistencyTests {

    private static final Logger log = LoggerFactory.getLogger(ConsistencyTests.class);

    private static Set<String> getVocabularyFolders() throws IOException {
        return Layout.getVocabularyFolders();
    }

    private final Validator validator = new Validator();

    @BeforeEach
    public void init() {
        SHACLPreferences.setProduceFailuresMode(true);
    }

    @ParameterizedTest(name = "Testing glossary {0}")
    @MethodSource("getVocabularyFolders")
    public void testGlossary(String folder) throws IOException {
        testFolder(folder,
            ".*-glosář(-[a-zA-Z0-9]+)?[.]ttl$",
            validator.getGlossaryRules()
        );
    }

    @ParameterizedTest(name = "Testing model {0}")
    @MethodSource("getVocabularyFolders")
    public void testModel(String folder) throws IOException {
        testFolder(folder,
            ".*-model(-[a-zA-Z0-9]+)?[.]ttl$",
            validator.getModelRules().stream()
                .filter(r -> !r.getPath().contains("m4.ttl")).collect(
                Collectors.toSet())
        );
    }

    @ParameterizedTest(name = "Testing vocabulary {0}")
    @MethodSource("getVocabularyFolders")
    public void testVocabulary(String folder) throws IOException {
        testFolder(folder,
            ".*-(glosář|slovník)(-[a-zA-Z0-9]+)?[.]ttl$",
            validator.getVocabularyRules()
        );
    }

    private void testFolder(String folder, String regex, Set<URL> rules) throws IOException {
        final Model dataModel =
            JenaUtil.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, null);

        OntDocumentManager.getInstance().setProcessImports(false);
        Arrays.stream(new File(folder)
            .listFiles((dir, name) -> name.matches(regex)))
            .forEach(f -> {
                log.info("Loading " + f);
                dataModel.read(f.getAbsolutePath(), null, FileUtils.langTurtle);
            });

        final ValidationReport r = validator.validate(dataModel, rules);
        r.results().forEach(result -> {
            getLoggingMethod(result).accept(MessageFormat
                .format("[{0}] Node {1} failing for value {2} with message: {3} ",
                    result.getSeverity().getLocalName(), result.getFocusNode(), result.getValue(),
                    result.getMessage()));
        });
        Assertions.assertTrue(r.results().stream().allMatch(
            res -> res.getSeverity().equals(SH.Warning) || res.getSeverity().equals(SH.Info)));
    }

    private Consumer<String> getLoggingMethod(final ValidationResult result) {
        if (result.getSeverity().equals(SH.Violation)) {
            return (s) -> log.error(s);
        } else if (result.getSeverity().equals(SH.Warning)) {
            return (s) -> log.warn(s);
        } else if (result.getSeverity().equals(SH.Info)) {
            return (s) -> log.info(s);
        } else {
            throw new IllegalArgumentException(result.getSeverity().getLocalName());
        }
    }
}