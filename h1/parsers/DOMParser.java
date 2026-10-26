package h1.parsers;

import h1.models.Product;
import h1.models.Warehouse;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMParser implements WarehouseParser {
    @Override
    public Warehouse parse(Path file) {
        Document doc;
        try {
            doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(file.toFile());
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        List<Product> products = parseProducts(doc);

        System.out.println(products);

        return null;
    }

    public List<Product> parseProducts(Document doc) {
        List<Product> products = new ArrayList<>();

        NodeList productList = doc.getElementsByTagName("products");
        for (int i = 0; i < productList.getLength(); i++) {
            Element productElement = (Element) productList.item(i);
            System.out.println(productElement);
            // products.add(new Product(
            //     productList.item(i).getAttributes().getNamedItem("id").getNodeValue(),
            //     productList.item(i).getAttributes().getNamedItem("name").getNodeValue(),
            //     productList.item(i).getAttributes().getNamedItem("sku").getNodeValue(),
            //     productList.item(i).getAttributes().getNamedItem("categoryId").getNodeValue()
            // ));
        }

        return products;
    }
}
