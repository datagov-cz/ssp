package cz.gov.ssp;

import static cz.gov.ssp.OntologyUtils.addTypeToModel;
import static org.junit.jupiter.api.Assertions.fail;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportSGoV {

    private static final Logger log = LoggerFactory.getLogger(ExportSGoV.class);

    @Test
    public void export() throws IOException {
        final Dataset dataset = DatasetFactory.create();
        Layout.getVocabularyFolders().forEach(vocabularyFolder -> {
            try {
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.glossary);
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.model);
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.vocabulary);
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.prilohy);
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.mapping);
            } catch (Exception e) {
                log.error("Error during processing of " + vocabularyFolder, e);
                fail();
            }
        });
        RDFDataMgr.write(new FileOutputStream("../sgov.trig"), dataset, RDFFormat.TRIG);

        exportGraphPerVocabulary(dataset);
    }

    private void exportGraphPerVocabulary(final Dataset fullDataset) throws FileNotFoundException {
        final Map<String, Model> map = new HashMap<>();
        fullDataset.listNames().forEachRemaining(g -> {
            String base = TestUtils.getVocabularyBase(g);
            Model m;
            if (map.containsKey(base)) {
                m = map.get(base);
            } else {
                m = ModelFactory.createDefaultModel();
                map.put(base, m);
            }
            m.add(fullDataset.getNamedModel(g));
        });
        final Dataset datasetSingle = DatasetFactory.create();
        datasetSingle.setDefaultModel(fullDataset.getDefaultModel());
        map.keySet().forEach(k -> datasetSingle.addNamedModel(k, map.get(k)));
        RDFDataMgr.write(new FileOutputStream("../sgov-graph-per-vocabulary.trig"), datasetSingle,
            RDFFormat.TRIG);
    }
}
