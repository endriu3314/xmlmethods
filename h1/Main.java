package h1;

import java.nio.file.Path;

import h1.models.Warehouse;
import h1.parsers.DOMParser;
import h1.parsers.SAXParser;
import h1.parsers.WarehouseParser;

public class Main {
    public static void main(String[] args) throws Exception {
        Path xmlDb = Path.of("data/warehouse.xml");
        
        WarehouseParser domParser = new DOMParser();
        Warehouse warehouseDom = domParser.parse(xmlDb);

        WarehouseParser saxParser = new SAXParser();
        Warehouse warehouseSax = saxParser.parse(xmlDb);

        System.out.println("\nQuery 1 - Stock per product");
        Queries.stockPerProduct(warehouseDom).forEach((product, quantity) ->
            System.out.printf("%-30s %s %d %n", product.name(), product.sku(), quantity));
        System.out.println();
        Queries.stockPerProduct(warehouseSax).forEach((product, quantity) ->
            System.out.printf("%-30s %s %d %n", product.name(), product.sku(), quantity));

        System.out.println("\nQuery 2 - Low stock alerts (< 100)");
        Queries.lowStockAlerts(warehouseDom, 100).forEach(alert ->
            System.out.printf("%-30s %6d -> %s @ %.2f %n",
                alert.product().name(),
                alert.quantity(),
                alert.cheapestSupplier() != null ? alert.cheapestSupplier().name() : "no supplier",
                alert.price()));
        System.out.println();
        Queries.lowStockAlerts(warehouseSax, 100).forEach(alert ->
            System.out.printf("%-30s %6d -> %s @ %.2f %n",
                alert.product().name(),
                alert.quantity(),
                alert.cheapestSupplier() != null ? alert.cheapestSupplier().name() : "no supplier",
                alert.price()));
    }
}
