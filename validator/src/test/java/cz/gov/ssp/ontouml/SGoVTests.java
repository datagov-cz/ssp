package cz.gov.ssp.ontouml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.util.SHACLPreferences;
import org.topbraid.shacl.validation.ValidationReport;
import org.topbraid.shacl.vocabulary.SH;

@Slf4j public class SGoVTests {

    private static Stream<String> getGlossaries() throws IOException {
        return Files.walk(Paths.get("../")).filter(Files::isRegularFile).map(Path::toFile)
                    .filter(f -> f.getName().endsWith("-glosář.ttl")).map(File::getAbsolutePath);
    }

    @BeforeEach public void init() {
        SHACLPreferences.setProduceFailuresMode(true);
    }

    @ParameterizedTest(name = "Testing glossary {0}") @MethodSource("getGlossaries")
    public void testGlossary(String glossaryFile) {
        final Validator validator = new Validator();
        final Model shapesModel = validator.getGlossaryRulesModel();

        final Model dataModel =
            JenaUtil.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, null);

        OntDocumentManager.getInstance().setProcessImports(false);
        dataModel.read(glossaryFile, null, FileUtils.langTurtle);

        final ValidationReport r = validator.validate(dataModel, shapesModel);

        r.results().forEach(result -> log.info(MessageFormat
            .format("[{0}] Node {1} failing for value {2} with message: {3} ",
                result.getSeverity().getLocalName(), result.getFocusNode(), result.getValue(),
                result.getMessage())));

        Assertions.assertTrue(r.results().stream().allMatch(
            res -> res.getSeverity().equals(SH.Warning) || res.getSeverity().equals(SH.Info)));
    }
}
