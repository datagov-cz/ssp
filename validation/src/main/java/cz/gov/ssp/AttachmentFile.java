package cz.gov.ssp;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public enum AttachmentFile {
    příloha;

    private Resource resource;

    AttachmentFile() {
        this.resource = ResourceFactory.createResource(Constants.POPIS_DAT_BASE + name());
    }

    public Resource getResource() {
        return resource;
    }
}
