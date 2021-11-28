package cz.gov.ssp;

import static cz.gov.ssp.OntologyUtils.addAttachmentToModel;
import static cz.gov.ssp.OntologyUtils.addVocabularyAttachmentsToModel;
import static cz.gov.ssp.OntologyUtils.addTypeToModel;
import static org.junit.jupiter.api.Assertions.fail;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
        Layout.getVocabularyFolders().forEach(folder -> {
            try {
                final List<String> vocabularyIris = addTypeToModel(dataset, folder, VocabularyFile.slovník);
                addTypeToModel(dataset, folder, VocabularyFile.glosář);
                addTypeToModel(dataset, folder, VocabularyFile.model);
                addTypeToModel(dataset, folder, VocabularyFile.mapování);

                if ( vocabularyIris.size() != 1) {
                    throw new Exception("Exactly one vocabulary must be present in the vocabulary file, but found " + vocabularyIris.size());
                }

                addVocabularyAttachmentsToModel(dataset, new SGoVIri(vocabularyIris.get(0)).extractBase(), folder);
            } catch (Exception e) {
                log.error("Error during processing of " + folder, e);
                fail();
            }
        });
        Layout.getAttachmentFolders().forEach(folder -> {
            try {
                addAttachmentToModel(dataset, folder, AttachmentFile.příloha);
            } catch (Exception e) {
                log.error("Error during processing of " + folder, e);
                fail();
            }
        });
        RDFDataMgr.write(new FileOutputStream("../sgov.trig"), dataset, RDFFormat.TRIG);

        exportGraphPerVocabulary(dataset);
    }

    private void exportGraphPerVocabulary(final Dataset fullDataset) throws FileNotFoundException {
        final Map<String, Model> map = new HashMap<>();
        fullDataset.listNames().forEachRemaining(name -> {
            final String base = new SGoVIri(name).extractBase();
            map.putIfAbsent(base, ModelFactory.createDefaultModel());
            map.get(base).add(fullDataset.getNamedModel(name));
        });
        final Dataset datasetSingle = DatasetFactory.create();
        datasetSingle.setDefaultModel(fullDataset.getDefaultModel());
        map.keySet().forEach(k -> datasetSingle.addNamedModel(k, map.get(k)));
        RDFDataMgr.write(new FileOutputStream("../sgov-graph-per-vocabulary.trig"), datasetSingle,
            RDFFormat.TRIG);
    }
}
