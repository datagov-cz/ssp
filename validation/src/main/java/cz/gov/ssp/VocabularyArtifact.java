package cz.gov.ssp;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public enum VocabularyArtifact {
    vocabulary("slovník"),
    glossary("glosář"),
    model("model"),
    prilohy("přílohy"),
    mapping("mapování");

    private String artifactIriLocalName;

    private Resource resource;

    VocabularyArtifact(final String artifactIriLocalName) {
        this.artifactIriLocalName = artifactIriLocalName;
        this.resource = ResourceFactory.createResource(
            "http://onto.fel.cvut.cz/ontologies/slovník/agendový/popis-dat/pojem/"+
                artifactIriLocalName);
    }

    public String getArtifactIriLocalName() {
        return artifactIriLocalName;
    }

    public Resource getResource() {
        return resource;
    }
}
