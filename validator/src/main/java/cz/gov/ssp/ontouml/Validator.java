package cz.gov.ssp.ontouml;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
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
    private static Set<String> vocabularyRules = new HashSet<>();

    static {
        for (int i = 1; i <= 10; i++) {
            glossaryRules.add("g" + i + ".ttl");
        }
        for (int i = 1; i <= 7; i++) {
            modelRules.add("m" + i + ".ttl");
        }
        for (int i = 1; i <= 1; i++) {
            modelRules.add("s" + i + ".ttl");
        }
    }

    public static Set<String> getModelRules() {
        return modelRules;
    }

    public static Set<String> getGlossaryRules() {
        return glossaryRules;
    }

    public static Set<String> getVocabularyRules() {
        return vocabularyRules;
    }

    public Model getRulesModel(final Collection<String> rules) {
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
}
