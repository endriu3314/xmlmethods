package h5.java;

import java.nio.file.Path;

import org.w3c.dom.NodeList;

public class Main {

    public static void main(String[] args) {
        XPath xp = new XPath(Path.of("data/warehouse.xml"));

        for (Expressions.Expr e : Expressions.ALL) {
            System.out.printf("%-3s %-7s %2d node(s)  %s%n", e.id(),
                e.category(), xp.nodes(e.xpath()).getLength(), e.description());
        }

        WarehouseApi api = new WarehouseApi(Path.of("data/warehouse.xml"));
        System.out.println("\n=== GET operations ===");
        route("GET /products", api.getProducts());
        route("GET /suppliers", api.getSuppliers());
        route("GET /stocks/quantities", api.getStockQuantities());
        route("GET /products/p7", api.getProduct("p7"));
        route("GET /stocks/low?threshold=50", api.getLowStock(50));
        route("GET /suppliers/by-country?country=Romania",
            api.getSuppliersByCountry("Romania"));
        route("GET /products/by-sku?prefix=ELC", api.getProductsBySkuPrefix("ELC"));
        route("GET /products/p8/suppliers", api.getSuppliersOfProduct("p8"));
        route("GET /locations/l5/products", api.getProductsAtLocation("l5"));
        route("GET /products/p8/cheapest-offer", api.getCheapestOffer("p8"));
        route("GET /categories/Tools/products", api.getProductsInCategory("Tools"));
        route("GET /products/no-such-id (missing)", api.getProduct("no-such-id"));
    }

    private static void route(String name, NodeList nodes) {
        System.out.printf("%-48s -> %d node(s)%n", name, nodes.getLength());
    }
}
