package cz.gov.ssp.validation;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateTrig {

    private static final Logger log = LoggerFactory.getLogger(GenerateTrig.class);

    private static final String gsp = "http://plugins.linkedpipes.com/ontology/x-graphStorePurger#";

    private Model createModel(final String vocabularyFolder, final VocabularyArtifact type)
        throws IOException {
        Model model = ModelFactory.createDefaultModel();
        final File[] files = new File(vocabularyFolder)
            .listFiles(f -> f.getName().contains(type.getArtifactIriLocalName()));
        for (File f : files) {
            model.read(new FileReader(f), null, "TURTLE");
        }
        return model;
    }

    private void addTypeToModel(final Dataset dataset,
                                final String vocabularyFolder,
                                final VocabularyArtifact type,
                                final Set<String> namedGraphs) throws IOException {
        Model model = createModel(vocabularyFolder, type);
        ResIterator st = model.listSubjectsWithProperty(RDF.type, type.getResource());
        if (!st.hasNext()) {
            log.warn("No {} found for {}", type, vocabularyFolder);
        } else {
            Resource r = st.nextResource();
            if (st.hasNext()) {
                log.error("Multiple resources of type {} found in {}", type, vocabularyFolder);
            } else {
                if (r != null) {
                    namedGraphs.add(r.getURI());
                    dataset.addNamedModel(r.getURI(), model);
                }
            }
        }
    }

    @Test
    public void generateTrig() throws IOException {
        final Dataset dataset = DatasetFactory.create();
        final Set<String> namedGraphs = new HashSet<>();
        Layout.getVocabularyFolders().forEach(vocabularyFolder -> {
            try {
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.glossary, namedGraphs);
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.model, namedGraphs);
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.vocabulary,
                    namedGraphs);
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.diagram, namedGraphs);
                addTypeToModel(dataset, vocabularyFolder, VocabularyArtifact.mapping, namedGraphs);
            } catch (Exception e) {
                log.error("Error during processing of " + vocabularyFolder, e);
                fail();
            }
        });
        RDFDataMgr.write(new FileOutputStream("../sgov.trig"), dataset, RDFFormat.TRIG);

        Model model = ModelFactory.createDefaultModel();
        Resource task = ResourceFactory.createResource(gsp + "Task");
        Property graph = ResourceFactory.createProperty(gsp + "graph");
        namedGraphs.stream().forEach(g -> {
            final Resource r = ResourceFactory.createResource("http://localhost/"+UUID.randomUUID());
            model.add(r, RDF.type, task);
            model.add(r, graph, ResourceFactory.createResource(g));
        });

        RDFDataMgr.write(new FileOutputStream("../sgov-named-graphs.ttl"), model, RDFFormat.TURTLE);
    }
}
