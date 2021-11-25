package cz.gov.ssp;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OntologyUtils {

    private static final Logger log = LoggerFactory.getLogger(OntologyUtils.class);

    /**
     * Loads a vocabulary artifact into a Jena model.
     *
     * @param vocabularyFolder  folder to look into
     * @param localArtifactName local artifact name
     * @return model containing the loaded artifact
     * @throws IOException
     */
    public static Model createModel(final String vocabularyFolder, final String localArtifactName)
        throws IOException {
        final Model model = ModelFactory.createDefaultModel();
        final File[] files = new File(vocabularyFolder)
            .listFiles(f -> f.getName().contains(localArtifactName));
        for (File f : files) {
            model.read(new FileReader(f), null, "TURTLE");
        }
        return model;
    }

    /**
     * Loads a file inside the vocabulary folder and stores it in a separate graph in the dataset.
     *
     * @param dataset          dataset to contribute to
     * @param vocabularyFolder folder with the vocabulary files
     * @param type             type of the vocabulary file
     *
     * @return IRIs of the instances of the type to add to the model.
     * @throws IOException
     */
    public static List<String> addTypeToModel(final Dataset dataset,
                                      final String vocabularyFolder,
                                      final VocabularyFile type) throws IOException {
        final Model model = createModel(vocabularyFolder, type.name());
        final ResIterator st = model.listSubjectsWithProperty(RDF.type, type.getResource());
        if (!st.hasNext()) {
            log.warn("No {} found for {}", type, vocabularyFolder);
            return Collections.emptyList();
        } else {
            return add(dataset, st, model);
        }
    }

    /**
     * Loads a file inside the vocabulary folder and stores it in a separate graph in the dataset.
     *
     * @param dataset          dataset to contribute to
     * @param vocabularyIri    IRI of the vocabulary to add attachments to
     * @param vocabularyFolder folder with the vocabulary files
     * @throws IOException
     */
    public static void addVocabularyAttachmentsToModel(final Dataset dataset,
                                                       final String vocabularyIri,
                                                       final String vocabularyFolder) throws IOException {
        final Model model = createModel(vocabularyFolder, VocabularyFile.přílohy.name());
        dataset.addNamedModel(model.createResource(vocabularyIri + "/přílohy").getURI(), model);
    }

    /**
     * Loads a file inside the vocabulary folder and stores it in a separate graph in the dataset.
     *
     * @param dataset          dataset to contribute to
     * @param vocabularyFolder folder with the vocabulary files
     * @param type             type of the vocabulary file
     * @throws IOException
     */
    public static void addAttachmentToModel(final Dataset dataset,
                                      final String vocabularyFolder,
                                      final AttachmentFile type) throws IOException {
        final Model model = createModel(vocabularyFolder, type.name());
        final ResIterator st = model.listSubjectsWithProperty(RDF.type, type.getResource());
        if (!st.hasNext()) {
            log.warn("No {} found for {}", type, vocabularyFolder);
        } else {
            add(dataset, st, model);
        }
    }

    private static List<String> add(final Dataset dataset, final ResIterator st, final Model model) {
        final List<String> uris = new ArrayList<>();
        if (st.hasNext()) {
            Resource r = st.nextResource();
            if (r != null) {
                dataset.addNamedModel(r.getURI(), model);
                uris.add(r.getURI());
            }
        }
        return uris;
    }
}
