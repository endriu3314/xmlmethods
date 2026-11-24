package h5.java;

import java.nio.file.Path;
import java.util.Map;

import org.w3c.dom.NodeList;

public class WarehouseApi {

    private final XPath xp;

    public WarehouseApi(Path xmlDb) {
        xp = new XPath(xmlDb);
    }

    public NodeList getProducts() {
        return xp.nodes("/warehouse/products/product");
    }

    public NodeList getSuppliers() {
        return xp.nodes("/warehouse/suppliers/supplier");
    }

    public NodeList getStockQuantities() {
        return xp.nodes("/warehouse/stocks/stock/@quantity");
    }

    public NodeList getProduct(String id) {
        return get("products.byId", "id", id);
    }

    public NodeList getLowStock(int threshold) {
        return get("stocks.belowThreshold", "threshold", threshold);
    }

    public NodeList getSuppliersByCountry(String country) {
        return get("suppliers.byCountry", "country", country);
    }

    public NodeList getProductsBySkuPrefix(String prefix) {
        return get("products.bySkuPrefix", "prefix", prefix);
    }

    public NodeList getSuppliersOfProduct(String id) {
        return get("suppliers.ofProduct", "id", id);
    }

    public NodeList getProductsAtLocation(String id) {
        return get("products.atLocation", "id", id);
    }

    public NodeList getCheapestOffer(String id) {
        return get("offers.cheapestForProduct", "id", id);
    }

    public NodeList getProductsInCategory(String name) {
        return get("products.inCategory", "name", name);
    }

    private NodeList get(String key, String variable, Object value) {
        return xp.nodes(Expressions.PARAMETRIZED.get(key), Map.of(variable, value));
    }
}
