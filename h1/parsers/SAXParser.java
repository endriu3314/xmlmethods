package h1.parsers;

import h1.models.Category;
import h1.models.Location;
import h1.models.LocationType;
import h1.models.Product;
import h1.models.ProductSupplier;
import h1.models.Stock;
import h1.models.Supplier;
import h1.models.Warehouse;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParser implements WarehouseParser {
    @Override
    public Warehouse parse(Path file) {
        WarehouseHandler handler = new WarehouseHandler();
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(file.toFile(), handler);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        return new Warehouse(
            handler.suppliers,
            handler.locations,
            handler.categories,
            handler.products,
            handler.productSuppliers,
            handler.stocks
        );
    }

    private static class WarehouseHandler extends DefaultHandler {
        private static final Set<String> RECORDS = Set.of(
            "supplier", "location", "category", "product", "product_supplier", "stock"
        );

        final List<Supplier> suppliers = new ArrayList<>();
        final List<Location> locations = new ArrayList<>();
        final List<Category> categories = new ArrayList<>();
        final List<Product> products = new ArrayList<>();
        final List<ProductSupplier> productSuppliers = new ArrayList<>();
        final List<Stock> stocks = new ArrayList<>();

        private final Map<String, String> fields = new HashMap<>();
        private final StringBuilder text = new StringBuilder();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            text.setLength(0);
            if (RECORDS.contains(qName)) {
                fields.clear();
                for (int i = 0; i < attributes.getLength(); i++) {
                    fields.put(attributes.getQName(i), attributes.getValue(i));
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            text.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            switch (qName) {
                case "supplier" -> suppliers.add(new Supplier(
                    fields.get("id"),
                    fields.get("name"),
                    String.join(", ", fields.get("street"), fields.get("city"), fields.get("country")),
                    fields.get("phone")
                ));
                case "location" -> locations.add(new Location(
                    fields.get("id"),
                    fields.get("name"),
                    fields.get("code"),
                    LocationType.valueOf(fields.get("type"))
                ));
                case "category" -> categories.add(new Category(
                    fields.get("id"),
                    fields.get("name")
                ));
                case "product" -> products.add(new Product(
                    fields.get("id"),
                    fields.get("name"),
                    fields.get("sku"),
                    fields.get("category_id")
                ));
                case "product_supplier" -> productSuppliers.add(new ProductSupplier(
                    fields.get("product_id"),
                    fields.get("supplier_id"),
                    Double.parseDouble(fields.get("price"))
                ));
                case "stock" -> stocks.add(new Stock(
                    fields.get("product_id"),
                    fields.get("location_id"),
                    Integer.parseInt(fields.get("quantity"))
                ));
                default -> fields.put(qName, text.toString().trim());
            }
            text.setLength(0);
        }
    }
}
