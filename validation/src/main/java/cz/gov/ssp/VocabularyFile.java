package cz.gov.ssp;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public enum VocabularyFile {
    slovník,
    glosář,
    model,
    mapování,
    přílohy;

    private static final String BASE = "http://onto.fel.cvut.cz/ontologies/slovník/agendový/popis-dat/pojem/";

    private Resource resource;

    VocabularyFile() {
        this.resource = ResourceFactory.createResource(BASE + name());
    }

    public Resource getResource() {
        return resource;
    }
}
