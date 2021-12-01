package cz.gov.ssp;

import static cz.gov.ssp.Constants.CONTAINER_IRI;
import static cz.gov.ssp.Constants.CONTAINER_TYPE;
import static cz.gov.ssp.Constants.REFERENCES_ATTACHMENT_CONTEXT_PROPERTY;
import static cz.gov.ssp.Constants.REFERENCES_CONTEXT_PROPERTY;
import static org.junit.jupiter.api.Assertions.fail;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
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

/**
 * Generates canonical container metadata referencing all available SSP vocabularies.
 * <p>
 * The canonical container is used to represent the SSP cache in the assembly line.
 */
public class GenerateCanonicalContainer {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateCanonicalContainer.class);

    @Test
    public void generateCanonicalWorkspace() throws IOException {
        LOG.info("Generating canonical container metadata with IRI {}.", CONTAINER_IRI);
        final Dataset dataset = DatasetFactory.create();
        final Model wsModel = ModelFactory.createDefaultModel();
        wsModel.add(wsModel.createResource(CONTAINER_IRI), RDF.type,
            wsModel.createResource(CONTAINER_TYPE));
        generateVocabularyReferences(wsModel);
        generateAttachmentReferences(wsModel);
        dataset.addNamedModel(CONTAINER_IRI, wsModel);
        RDFDataMgr.write(new FileOutputStream("../sgov-canonical-container.trig"), dataset,
            RDFFormat.TRIG);
    }

    private void generateVocabularyReferences(Model wsModel) throws IOException {
        generateReferences(wsModel,
            Layout.getVocabularyFolders(),
            VocabularyFile.slovník.name(),
            VocabularyFile.slovník.getResource(),
            REFERENCES_CONTEXT_PROPERTY );
    }

    private void generateAttachmentReferences(Model wsModel) throws IOException {
        generateReferences(wsModel,
            Layout.getAttachmentFolders(),
            AttachmentFile.příloha.name(),
            AttachmentFile.příloha.getResource(),
            REFERENCES_ATTACHMENT_CONTEXT_PROPERTY );
    }

    private void generateReferences(Model wsModel, final Set<String> folders,
                                    String typeName,
                                    Resource type,
                                    String property) {
        folders.forEach(folder -> {
            try {
                final Model vocModel = OntologyUtils.createModel(folder,
                    typeName);
                final StmtIterator it = vocModel
                    .listStatements(null, RDF.type, type);
                while (it.hasNext()) {
                    final Resource iri = it.nextStatement().getSubject();
                    LOG.debug("Adding reference to {}.", iri);
                    wsModel.add(wsModel.createResource(CONTAINER_IRI),
                        wsModel.createProperty(property), iri);
                }
            } catch (Exception e) {
                LOG.error("Error during processing of " + folder, e);
                fail();
            }
        });
    }
}
