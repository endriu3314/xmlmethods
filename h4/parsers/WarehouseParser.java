package h4.parsers;

import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import h4.models.Warehouse;

public interface WarehouseParser {
    Warehouse parse(Path file);

    void validate(Path file);

    static Schema schemaFor(Path xmlFile) {
        try {
            return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                .newSchema(xmlFile.resolveSibling("warehouse.xsd").toFile());
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
