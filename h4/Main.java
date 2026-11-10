package h4;

import java.nio.file.Path;

import h4.models.Warehouse;
import h4.parsers.DOMParser;
import h4.parsers.SAXParser;
import h4.parsers.WarehouseParser;

public class Main {
    public static void main(String[] args) throws Exception {
        Path xmlDb = Path.of("data/warehouse.xml");
        Path invalidDb = Path.of("data/warehouse_invalid.xml");

        WarehouseParser domParser = new DOMParser();
        WarehouseParser saxParser = new SAXParser();

        domParser.validate(xmlDb);
        System.out.println("DOM: " + xmlDb + " is valid");
        saxParser.validate(xmlDb);
        System.out.println("SAX: " + xmlDb + " is valid");

        System.out.println();
        
        try {
            saxParser.validate(invalidDb);
            System.out.println("SAX: " + invalidDb + " accepted (unexpected)");
        } catch (RuntimeException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            System.out.println("SAX: rejected " + invalidDb + " -> " + cause.getMessage());
        }

        try {
            domParser.parse(invalidDb);
            System.out.println("DOM: " + invalidDb + " accepted (unexpected)");
        } catch (RuntimeException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            System.out.println("DOM: rejected " + invalidDb + " -> " + cause.getMessage());
        }

        Warehouse warehouseDom = domParser.parse(xmlDb);
        Warehouse warehouseSax = saxParser.parse(xmlDb);

        System.out.printf("%nParsed: %d suppliers, %d locations, %d categories, "
                + "%d products, %d product_suppliers, %d stocks (DOM), %d stocks (SAX)%n",
            warehouseDom.suppliers().size(),
            warehouseDom.locations().size(),
            warehouseDom.categories().size(),
            warehouseDom.products().size(),
            warehouseDom.productSuppliers().size(),
            warehouseDom.stocks().size(),
            warehouseSax.stocks().size());
    }
}
