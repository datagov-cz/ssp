package cz.gov.ssp.ontouml;

import java.text.MessageFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.util.SHACLPreferences;
import org.topbraid.shacl.validation.ValidationReport;
import org.topbraid.shacl.vocabulary.SH;

@Slf4j public class RulesTest {

    @BeforeEach public void init() {
        SHACLPreferences.setProduceFailuresMode(true);
    }

    @ParameterizedTest(name = "Rule {0} for {1} (should be {2})")
    @CsvFileSource(resources = "/test-cases.csv", numLinesToSkip = 1)
    public void testShaclRule(String rule, String output, String outcome) {
        log.info("Rule {}", rule);
        final Model shapesModel = JenaUtil.createMemoryModel();
        shapesModel.read(RulesTest.class.getResourceAsStream("/" + rule), "urn:dummy",
            FileUtils.langTurtle);
        testModel(shapesModel, output, Outcome.valueOf(outcome));
    }

    private void testModel(Model shapesModel, String data, Outcome outcome) {
        log.info("- expects {} for data {}", outcome, data);

        final Model dataModel =
            JenaUtil.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, null);

        OntDocumentManager.getInstance().setProcessImports(false);
        dataModel.read(RulesTest.class.getResourceAsStream("/" + data), "urn:dummy",
            FileUtils.langTurtle);

        final Validator validator = new Validator();
        final ValidationReport r = validator.validate(dataModel, shapesModel);

        r.results().forEach(result -> log.info(MessageFormat
            .format("[{0}] Node {1} failing for value {2} with message: {3} ",
                result.getSeverity().getLocalName(), result.getFocusNode(), result.getValue(),
                result.getMessage())));

        if (r.conforms()) {
            Assertions.assertEquals(Outcome.Pass, outcome);
        } else {
            Assertions.assertTrue(
                r.results().stream().anyMatch(a -> a.getSeverity().equals(outcome.url)));
        }
    }

    enum Outcome {
        Info(SH.Info), Warning(SH.Warning), Violation(SH.Violation), Pass(null);
        Resource url;

        Outcome(Resource url) {
            this.url = url;
        }
    }
}
