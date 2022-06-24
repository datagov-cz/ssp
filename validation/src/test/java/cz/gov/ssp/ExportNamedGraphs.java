package cz.gov.ssp;

import static cz.gov.ssp.Constants.GSP;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ExportNamedGraphs {

    private static final String queryString = "SELECT DISTINCT ?graph\n"
        + "WHERE { GRAPH ?graph { ?s ?p ?o . } } ORDER BY DESC(?graph)";

    private static String service;

    private static String outputFile;

    @BeforeAll
    public static void init() {
        if (!System.getenv().containsKey("SGOV_SPARQL_SERVICE")) {
            throw new IllegalArgumentException(
                "Environmental variable SGOV_SPARQL_SERVICE must be set.");
        }

        if (!System.getenv().containsKey("SGOV_SPARQL_SERVICE_NAMED_GRAPHS_FILE")) {
            throw new IllegalArgumentException(
                "Environmental variable SGOV_SPARQL_SERVICE_NAMED_GRAPHS_FILE must be set.");
        }

        service = System.getenv("SGOV_SPARQL_SERVICE");
        outputFile = System.getenv("SGOV_SPARQL_SERVICE_NAMED_GRAPHS_FILE");
    }

    @Test
    void export() throws IOException {
        final QueryExecution qexec = QueryExecutionFactory.sparqlService(service, queryString);
        final ResultSet results = qexec.execSelect();
        final Model model = ModelFactory.createDefaultModel();
        final Resource task = ResourceFactory.createResource(GSP + "Task");
        final Property graph = ResourceFactory.createProperty(GSP + "graph");
        while (results.hasNext()) {
            final QuerySolution n = results.next();
            final Resource r =
                ResourceFactory.createResource("http://localhost/" + UUID.randomUUID());
            model.add(r, RDF.type, task);
            model.add(r, graph, n.getResource("graph"));
        }

        final File f = new File(outputFile);
        try(final FileOutputStream fos = new FileOutputStream(f)){
            RDFDataMgr.write(fos, model,
                RDFFormat.TURTLE);
        }
    }
}
