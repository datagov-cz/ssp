package cz.gov.ssp.ontouml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileUtils;
import org.topbraid.jenax.progress.SimpleProgressMonitor;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.rules.RuleUtil;
import org.topbraid.shacl.validation.ResourceValidationReport;
import org.topbraid.shacl.validation.ValidationReport;
import org.topbraid.shacl.validation.ValidationUtil;

@Slf4j public class Validator {

    private static Set<String> glossaryRules = new HashSet<>();
    private static Set<String> modelRules = new HashSet<>();

    static {
        for (int i = 1; i <= 9; i++) {
            glossaryRules.add("g" + i + ".ttl");
        }
        for (int i = 1; i <= 7; i++) {
            modelRules.add("m" + i + ".ttl");
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            return;
        }
        if (args[0].equals("g")) {
            String arg = args[1];
            Validator v = new Validator();
            v.checkGlossary(arg);
        }
    }

    public Model getGlossaryRulesModel() {
        return getRulesModel(glossaryRules);
    }

    private Model getRulesModel(final Collection<String> rules) {
        final Model shapesModel = JenaUtil.createMemoryModel();
        rules.forEach(r -> shapesModel
            .read(Validator.class.getResourceAsStream("/" + r), null, FileUtils.langTurtle));
        return shapesModel;
    }

    public ValidationReport validate(final Model dataModel, final Model shapesModel) {
        log.info("Validating model {}", dataModel);
        shapesModel.read(Validator.class.getResourceAsStream("/inference-rules.ttl"), null,
            FileUtils.langTurtle);

        Model inferredModel = RuleUtil
            .executeRules(dataModel, shapesModel, null, new SimpleProgressMonitor("inference"));
        dataModel.add(inferredModel);

        final Resource report = ValidationUtil.validateModel(dataModel, shapesModel, true);

        return new ResourceValidationReport(report);
    }

    public void checkGlossary(String file) throws FileNotFoundException {
        final Model dataModel =
            JenaUtil.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, null);

        OntDocumentManager.getInstance().setProcessImports(false);
        dataModel.read(new FileReader(file), null, FileUtils.langTurtle);

        final Validator validator = new Validator();
        final ValidationReport r = validator.validate(dataModel, getGlossaryRulesModel());

        r.results().forEach(result -> log.info(MessageFormat
            .format("[{0}] Node {1} failing for value {2} with message: {3} ",
                result.getSeverity().getLocalName(), result.getFocusNode(), result.getValue(),
                result.getMessage())));
    }
}
