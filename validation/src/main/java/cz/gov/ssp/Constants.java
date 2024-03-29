package cz.gov.ssp;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;

public class Constants {

    private Constants(){
    }

    public static final String SLOVNIK_GOV_CZ_BASE = "https://slovník.gov.cz/";
    public static final String POPIS_DAT_BASE = "http://onto.fel.cvut.cz/ontologies/slovník/agendový/popis-dat/pojem/";
    public static final String PRACOVNI_PROSTOR_BASE = "https://slovník.gov.cz/datový/pracovní-prostor/pojem/";
    public static final String REFERENCES_CONTEXT_PROPERTY = PRACOVNI_PROSTOR_BASE + "odkazuje-na-kontext";
    public static final String REFERENCES_ATTACHMENT_CONTEXT_PROPERTY = PRACOVNI_PROSTOR_BASE + "odkazuje-na-přílohový-kontext";
    public static final String CONTAINER_TYPE = PRACOVNI_PROSTOR_BASE + "kanonický-kontejner";
    public static final String CONTAINER_IRI = "https://slovník.gov.cz";
    public static final String GSP = "http://plugins.linkedpipes.com/ontology/x-graphStorePurger#";
    public static final Property
        MA_PRILOHU = ResourceFactory.createProperty(POPIS_DAT_BASE + "má-přílohu");
}
