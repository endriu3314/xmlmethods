package h4.parsers;

import h4.models.Category;
import h4.models.Location;
import h4.models.LocationType;
import h4.models.Product;
import h4.models.ProductSupplier;
import h4.models.Stock;
import h4.models.Supplier;
import h4.models.Warehouse;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DOMParser implements WarehouseParser {
    @Override
    public Warehouse parse(Path file) {
        Document doc;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setSchema(WarehouseParser.schemaFor(file));

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {
                @Override public void warning(SAXParseException e) { }
                @Override public void error(SAXParseException e) throws SAXException { throw e; }
                @Override public void fatalError(SAXParseException e) throws SAXException { throw e; }
            });

            doc = builder.parse(file.toFile());
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        return new Warehouse(
            parseSuppliers(doc),
            parseLocations(doc),
            parseCategories(doc),
            parseProducts(doc),
            parseProductSuppliers(doc),
            parseStocks(doc)
        );
    }

    @Override
    public void validate(Path file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = factory.newDocumentBuilder().parse(file.toFile());
            WarehouseParser.schemaFor(file)
                .newValidator()
                .validate(new DOMSource(doc));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private static String childText(Element parent, String tagName) {
        return parent.getElementsByTagName(tagName).item(0).getTextContent().trim();
    }

    public List<Supplier> parseSuppliers(Document doc) {
        List<Supplier> suppliers = new ArrayList<>();

        NodeList supplierList = doc.getElementsByTagName("supplier");
        for (int i = 0; i < supplierList.getLength(); i++) {
            Element supplierElement = (Element) supplierList.item(i);
            Element addressElement = (Element) supplierElement.getElementsByTagName("address").item(0);

            String address = String.join(", ",
                childText(addressElement, "street"),
                childText(addressElement, "city"),
                childText(addressElement, "country"));

            suppliers.add(new Supplier(
                supplierElement.getAttribute("id"),
                childText(supplierElement, "name"),
                address,
                childText(supplierElement, "phone")
            ));
        }

        return suppliers;
    }

    public List<Product> parseProducts(Document doc) {
        List<Product> products = new ArrayList<>();

        NodeList productList = doc.getElementsByTagName("product");
        for (int i = 0; i < productList.getLength(); i++) {
            Element productElement = (Element) productList.item(i);
            products.add(new Product(
                productElement.getAttribute("id"),
                childText(productElement, "name"),
                childText(productElement, "sku"),
                productElement.getAttribute("category_id")
            ));
        }

        return products;
    }

    public List<Location> parseLocations(Document doc) {
        List<Location> locations = new ArrayList<>();

        NodeList locationList = doc.getElementsByTagName("location");
        for (int i = 0; i < locationList.getLength(); i++) {
            Element locationElement = (Element) locationList.item(i);
            locations.add(new Location(
                locationElement.getAttribute("id"),
                childText(locationElement, "name"),
                childText(locationElement, "code"),
                LocationType.valueOf(childText(locationElement, "type"))
            ));
        }

        return locations;
    }

    public List<Category> parseCategories(Document doc) {
        List<Category> categories = new ArrayList<>();

        NodeList categoryList = doc.getElementsByTagName("category");
        for (int i = 0; i < categoryList.getLength(); i++) {
            Element categoryElement = (Element) categoryList.item(i);
            categories.add(new Category(
                categoryElement.getAttribute("id"),
                childText(categoryElement, "name")
            ));
        }

        return categories;
    }

    public List<ProductSupplier> parseProductSuppliers(Document doc) {
        List<ProductSupplier> productSuppliers = new ArrayList<>();

        NodeList productSupplierList = doc.getElementsByTagName("product_supplier");
        for (int i = 0; i < productSupplierList.getLength(); i++) {
            Element productSupplierElement = (Element) productSupplierList.item(i);
            productSuppliers.add(new ProductSupplier(
                productSupplierElement.getAttribute("product_id"),
                productSupplierElement.getAttribute("supplier_id"),
                Double.parseDouble(childText(productSupplierElement, "price"))
            ));
        }

        return productSuppliers;
    }

    public List<Stock> parseStocks(Document doc) {
        List<Stock> stocks = new ArrayList<>();

        NodeList stockList = doc.getElementsByTagName("stock");
        for (int i = 0; i < stockList.getLength(); i++) {
            Element stockElement = (Element) stockList.item(i);
            stocks.add(new Stock(
                stockElement.getAttribute("product_id"),
                stockElement.getAttribute("location_id"),
                Integer.parseInt(stockElement.getAttribute("quantity"))
            ));
        }

        return stocks;
    }
}
