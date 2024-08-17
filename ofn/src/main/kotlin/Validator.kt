import org.apache.jena.ontology.OntDocumentManager
import org.apache.jena.ontology.OntModelSpec
import org.apache.jena.rdf.model.Model
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.riot.Lang
import org.apache.jena.riot.RDFDataMgr
import org.apache.jena.shacl.ShaclValidator
import org.apache.jena.shacl.Shapes
import org.apache.jena.shacl.ValidationReport
import org.apache.jena.shacl.lib.ShLib
import java.util.*

class Validator {

    /**
     * Validates the given Model against the OFN SHACL shape.
     */
    fun validateTurtle(dataUri : String): ValidationReport {
        val dataModel: Model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF)
        OntDocumentManager.getInstance().processImports = false
        dataModel.read(dataUri, "TURTLE")

        val shapesUri = Objects.requireNonNull(javaClass.getResource("/ofn-slovniky-shacl.ttl")).toURI()
        val shapesModel = ModelFactory.createDefaultModel()
        OntDocumentManager.getInstance().processImports = false
        shapesModel.read(shapesUri.toString())

        val shapes = Shapes.parse(shapesModel.graph)

        val report = ShaclValidator.get().validate(shapes, dataModel.graph)
        ShLib.printReport(report)
        RDFDataMgr.write(System.out, report.model, Lang.TTL)
        return report;
    }
}