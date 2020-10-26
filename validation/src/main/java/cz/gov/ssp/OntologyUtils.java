package cz.gov.ssp;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    public static Model createModel(final String vocabularyFolder, final VocabularyArtifact type)
        throws IOException {
        Model model = ModelFactory.createDefaultModel();
        final File[] files = new File(vocabularyFolder)
            .listFiles(f -> f.getName().contains(type.getArtifactIriLocalName()));
        for (File f : files) {
            model.read(new FileReader(f), null, "TURTLE");
        }
        return model;
    }

    public static void addTypeToModel(final Dataset dataset,
                                final String vocabularyFolder,
                                final VocabularyArtifact type) throws IOException {
        Model model = createModel(vocabularyFolder, type);
        ResIterator st = model.listSubjectsWithProperty(RDF.type, type.getResource());
        if (!st.hasNext()) {
            log.warn("No {} found for {}", type, vocabularyFolder);
        } else {
            while(st.hasNext()) {
                Resource r = st.nextResource();
                if (r != null) {
                    dataset.addNamedModel(r.getURI(), model);
                }
            }
        }
    }
}
