package cz.gov.ssp.validation;

import cz.gov.ssp.Layout;
import cz.gov.ssp.OntologyUtils;
import cz.gov.ssp.VocabularyArtifact;
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
 * Generates canonical workspace metadata referencing all available SSP vocabularies.
 */
public class GenerateCanonicalWorkspace {

    public static final String WORKSPACE_IRI = "https://slovník.gov.cz";

    private static final Logger LOG = LoggerFactory.getLogger(GenerateCanonicalWorkspace.class);

    @Test
    public void generateCanonicalWorkspace() throws IOException {
        LOG.info("Generating canonical workspace metadata with IRI {}.", WORKSPACE_IRI);
        final Dataset dataset = DatasetFactory.create();
        final Model wsModel = ModelFactory.createDefaultModel();
        wsModel.add(wsModel.createResource(WORKSPACE_IRI), RDF.type,
                wsModel.createResource("https://slovník.gov.cz/datový/pracovní-prostor/pojem/metadatový-kontext"));
        generateVocabularyReferences(wsModel);
        dataset.addNamedModel(WORKSPACE_IRI, wsModel);
        RDFDataMgr.write(new FileOutputStream("../sgov-workspace.trig"), dataset, RDFFormat.TRIG);
    }

    private void generateVocabularyReferences(Model wsModel) throws IOException {
        Layout.getVocabularyFolders().forEach(vocabularyFolder -> {
            try {
                final Model vocModel = OntologyUtils.createModel(vocabularyFolder, VocabularyArtifact.vocabulary);
                final StmtIterator it = vocModel.listStatements(null, RDF.type, vocModel.createResource(
                        "http://onto.fel.cvut.cz/ontologies/slovník/agendový/popis-dat/pojem/slovník"));
                while (it.hasNext()) {
                    final Resource vocabularyIri = it.nextStatement().getSubject();
                    LOG.debug("Adding reference to vocabulary {}.", vocabularyIri);
                    wsModel.add(wsModel.createResource(WORKSPACE_IRI), wsModel.createProperty(
                            "https://slovník.gov.cz/datový/pracovní-prostor/pojem/odkazuje-na-kontext"),
                            vocabularyIri);
                }
            } catch (Exception e) {
                LOG.error("Error during processing of " + vocabularyFolder, e);
                fail();
            }
        });
    }
}
