package cz.gov.ssp;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public enum AttachmentFile {
    priloha("příloha");

    private static final String BASE = "http://onto.fel.cvut.cz/ontologies/slovník/agendový/popis-dat/pojem/";

    private String localName;

    private Resource resource;

    AttachmentFile(final String localName) {
        this.localName = localName;
        this.resource = ResourceFactory.createResource(BASE + localName);
    }

    public String getLocalName() {
        return localName;
    }

    public Resource getResource() {
        return resource;
    }
}
