package cz.gov.ssp;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Generates canonical container metadata referencing all available SSP vocabularies.
 *
 * The canonical container is used to represent the SSP cache in the assembly line.
 */
public class GenerateCanonicalContainer {

    private static final String CONTAINER_TYPE = "https://slovník.gov.cz/datový/pracovní-prostor/pojem/kanonický-kontejner";
    private static final String REFERENCES_CONTEXT_PROPERTY = "https://slovník.gov.cz/datový/pracovní-prostor/pojem/odkazuje-na-kontext";
    public static final String CONTAINER_IRI = "https://slovník.gov.cz";

    private static final Logger LOG = LoggerFactory.getLogger(GenerateCanonicalContainer.class);

    @Test
    public void generateCanonicalWorkspace() throws IOException {
        LOG.info("Generating canonical container metadata with IRI {}.", CONTAINER_IRI);
        final Dataset dataset = DatasetFactory.create();
        final Model wsModel = ModelFactory.createDefaultModel();
        wsModel.add(wsModel.createResource(CONTAINER_IRI), RDF.type, wsModel.createResource(CONTAINER_TYPE));
        generateVocabularyReferences(wsModel);
        dataset.addNamedModel(CONTAINER_IRI, wsModel);
        RDFDataMgr.write(new FileOutputStream("../sgov-canonical-container.trig"), dataset, RDFFormat.TRIG);
    }

    private void generateVocabularyReferences(Model wsModel) throws IOException {
        Layout.getVocabularyFolders().forEach(vocabularyFolder -> {
            try {
                final Model vocModel = OntologyUtils.createModel(vocabularyFolder, VocabularyArtifact.vocabulary);
                final StmtIterator it = vocModel
                        .listStatements(null, RDF.type, VocabularyArtifact.vocabulary.getResource());
                while (it.hasNext()) {
                    final Resource vocabularyIri = it.nextStatement().getSubject();
                    LOG.debug("Adding reference to vocabulary {}.", vocabularyIri);
                    wsModel.add(wsModel.createResource(CONTAINER_IRI),
                            wsModel.createProperty(REFERENCES_CONTEXT_PROPERTY), vocabularyIri);
                }
            } catch (Exception e) {
                LOG.error("Error during processing of " + vocabularyFolder, e);
                fail();
            }
        });
    }
}
