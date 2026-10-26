package h1.models;

import java.util.List;

public record Warehouse(
        List<Supplier> suppliers,
        List<Location> locations,
        List<Category> categories,
        List<Product> products,
        List<ProductSupplier> productSuppliers,
        List<Stock> stocks) {
}