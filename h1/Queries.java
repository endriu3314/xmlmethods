package h1;

import h1.models.Product;
import h1.models.ProductSupplier;
import h1.models.Stock;
import h1.models.Supplier;
import h1.models.Warehouse;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Queries {

    public static Map<Product, Integer> stockPerProduct(Warehouse warehouse) {
        Map<Product, Integer> result = new LinkedHashMap<>();
        for (Product product : warehouse.products()) {
            int total = warehouse.stocks().stream()
                .filter(stock -> stock.productId().equals(product.id()))
                .mapToInt(Stock::quantity)
                .sum();
            result.put(product, total);
        }
        return result;
    }

    public record LowStockAlert(Product product, int quantity, Supplier cheapestSupplier, double price) {
    }

    public static List<LowStockAlert> lowStockAlerts(Warehouse warehouse, int threshold) {
        return stockPerProduct(warehouse).entrySet().stream()
            .filter(entry -> entry.getValue() < threshold)
            .map(entry -> {
                Product product = entry.getKey();
                Optional<ProductSupplier> cheapest = warehouse.productSuppliers().stream()
                    .filter(ps -> ps.productId().equals(product.id()))
                    .min(Comparator.comparingDouble(ProductSupplier::price));
                Supplier supplier = cheapest
                    .flatMap(ps -> warehouse.suppliers().stream()
                        .filter(s -> s.id().equals(ps.supplierId()))
                        .findFirst())
                    .orElse(null);
                return new LowStockAlert(product, entry.getValue(), supplier, cheapest.map(ProductSupplier::price).orElse(0.0));
            })
            .sorted(Comparator.comparingInt(LowStockAlert::quantity))
            .toList();
    }
}
