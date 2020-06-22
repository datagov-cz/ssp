package cz.gov.ssp.validation;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public enum VocabularyArtifact {
    vocabulary("slovník"),
    glossary("glosář"),
    model("model"),
    diagram("diagram"),
    mapping("mapování");

    private String artifactIriLocalName;

    private Resource resource;

    VocabularyArtifact(String artifactIriLocalName) {
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
