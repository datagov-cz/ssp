import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.update.UpdateAction
import org.apache.jena.update.UpdateFactory
import java.io.File
import java.io.FileWriter
import java.nio.charset.Charset

class TransformToOfn

fun main() {
    val dir = "content/vocabularies"
    val glossaryUrl = "$dir/v-sgov/v-sgov-glosář.ttl"
    val outputUrl = "$dir/v-sgov/v-sgov-glosář-ofn.ttl"

    val model = ModelFactory.createDefaultModel()
    model.read(File(glossaryUrl).toURI().toString())

    val list = listOf(
        "updates/glossary-change-dcterms:title-to-skos:preflabel.rq",
        "updates/glossary-fix-dcterms:created-datatype.rq"
    )

    val request = UpdateFactory.create()

    list.forEach { request.add(TransformToOfn::class.java.getResource(it)?.readText(Charset.defaultCharset())) }
    UpdateAction.execute(request, model);

    model.write(FileWriter(outputUrl), "TURTLE")
}
