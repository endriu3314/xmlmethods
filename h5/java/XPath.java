package h5.java;

import java.nio.file.Path;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathVariableResolver;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XPath {

    private final Document document;

    public XPath(Path xmlDb) {
        try {
            document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(xmlDb.toFile());
        } catch (Exception e) {
            throw new RuntimeException("Cannot load " + xmlDb, e);
        }
    }

    public synchronized NodeList nodes(String expression) {
        return nodes(expression, Map.of());
    }

    public synchronized NodeList nodes(String expression, Map<String, Object> variables) {
        try {
            javax.xml.xpath.XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setXPathVariableResolver(new XPathVariableResolver() {
                public Object resolveVariable(QName name) {
                    return variables.get(name.getLocalPart());
                }
            });
            return (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);
        } catch (Exception e) {
            throw new RuntimeException("Bad XPath: " + expression, e);
        }
    }
}
