package cz.gov.ssp.validation;

import static cz.gov.ssp.OntologyUtils.addTypeToModel;
import static org.junit.jupiter.api.Assertions.fail;

import cz.gov.ssp.Layout;
import cz.gov.ssp.TestUtils;
import cz.gov.ssp.VocabularyArtifact;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Export {

    private static final Logger log = LoggerFactory.getLogger(Export.class);

    private static final String gsp = "http://plugins.linkedpipes.com/ontology/x-graphStorePurger#";

    @Test
    public void export() throws IOException {
        final Dataset dataset = DatasetFactory.create();
        Layout.getVocabularyFolders().forEach(vocabularyFolder -> {
            try {
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.glossary);
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.model);
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.vocabulary);
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.diagram);
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.mapping);
            } catch (Exception e) {
                log.error("Error during processing of " + vocabularyFolder, e);
                fail();
            }
        });
        RDFDataMgr.write(new FileOutputStream("../sgov.trig"), dataset, RDFFormat.TRIG);

        exportGraphPerVocabulary(dataset);

        exportGraphList(dataset);
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

    private void exportGraphList(final Dataset fullDataset) throws FileNotFoundException {
        Model model = ModelFactory.createDefaultModel();
        Resource task = ResourceFactory.createResource(gsp + "Task");
        Property graph = ResourceFactory.createProperty(gsp + "graph");
        fullDataset.listNames().forEachRemaining(g -> {
            final Resource r =
                ResourceFactory.createResource("http://localhost/" + UUID.randomUUID());
            model.add(r, RDF.type, task);
            model.add(r, graph, ResourceFactory.createResource(g));
        });

        RDFDataMgr.write(new FileOutputStream("../sgov-named-graphs.ttl"), model, RDFFormat.TURTLE);
    }
}
