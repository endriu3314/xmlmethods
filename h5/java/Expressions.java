package h5.java;

import java.util.List;
import java.util.Map;

public class Expressions {

    public enum Category { SIMPLE, MEDIUM, COMPLEX }

    public record Expr(String id, Category category, String description, String xpath) {
    }

    public static final List<Expr> ALL = List.of(
        new Expr("S1", Category.SIMPLE,
            "All product elements (absolute path, child axis)",
            "/warehouse/products/product"),
        new Expr("S2", Category.SIMPLE,
            "Names of all suppliers (descendant axis)",
            "//supplier/name"),
        new Expr("S3", Category.SIMPLE,
            "Quantity attribute of every stock entry (attribute axis)",
            "/warehouse/stocks/stock/@quantity"),
        new Expr("S4", Category.SIMPLE,
            "Location elements via the parent axis",
            "//type/.."),
        new Expr("S5", Category.SIMPLE,
            "Category names as text nodes",
            "/warehouse/categories/category/name/text()"),
        new Expr("S6", Category.SIMPLE,
            "Country of every supplier address",
            "//address/country"),
        new Expr("S7", Category.SIMPLE,
            "Category foreign key of every product (attribute axis)",
            "//product/@category_id"),

        new Expr("M1", Category.MEDIUM,
            "Product selected by its id attribute",
            "//product[@id='p7']"),
        new Expr("M2", Category.MEDIUM,
            "First stock entry in document order (positional)",
            "//stocks/stock[1]"),
        new Expr("M3", Category.MEDIUM,
            "Last location (positional last())",
            "//location[last()]"),
        new Expr("M4", Category.MEDIUM,
            "Stock entries with fewer than 50 units",
            "//stock[@quantity < 50]"),
        new Expr("M5", Category.MEDIUM,
            "Suppliers whose address is in Romania (condition on child element)",
            "//supplier[address/country='Romania']"),
        new Expr("M6", Category.MEDIUM,
            "Offers from supplier s3 with a price above 50 (multiple conditions)",
            "//product_supplier[@supplier_id='s3' and price > 50]"),
        new Expr("M7", Category.MEDIUM,
            "Products whose SKU starts with 'ELC' (function in predicate)",
            "//product[starts-with(sku, 'ELC')]"),
        new Expr("M8", Category.MEDIUM,
            "Products referencing category c1 (foreign-key attribute equality)",
            "//product[@category_id='c1']"),

        new Expr("C1", Category.COMPLEX,
            "Products that have at least one stock entry (product <- stock)",
            "//product[@id = //stock/@product_id]"),
        new Expr("C2", Category.COMPLEX,
            "Suppliers that offer product p8 (supplier <- product_supplier)",
            "//supplier[@id = //product_supplier[@product_id='p8']/@supplier_id]"),
        new Expr("C3", Category.COMPLEX,
            "Products in the category named 'Tools' (product -> category)",
            "//product[@category_id = //category[name='Tools']/@id]"),
        new Expr("C4", Category.COMPLEX,
            "Stock rows of the product with SKU 'TLS-0002' (stock -> product)",
            "//stock[@product_id = //product[sku='TLS-0002']/@id]"),
        new Expr("C5", Category.COMPLEX,
            "Locations holding a stock entry below 20 units (location <- stock)",
            "//location[@id = //stock[@quantity < 20]/@location_id]"),
        new Expr("C6", Category.COMPLEX,
            "Cheapest offer for product p8 (nested price comparison)",
            "//product_supplier[@product_id='p8']"
                + "[not(price > //product_supplier[@product_id='p8']/price)]"),
        new Expr("C7", Category.COMPLEX,
            "Suppliers of products in the 'Electronics' category (3 hops)",
            "//supplier[@id = //product_supplier[@product_id"
                + " = //product[@category_id = //category[name='Electronics']/@id]/@id]"
                + "/@supplier_id]"),
        new Expr("C8", Category.COMPLEX,
            "Products stored in EXPORT locations (2 hops)",
            "//product[@id = //stock[@location_id = //location[type='EXPORT']/@id]"
                + "/@product_id]"));

    public static final Map<String, String> PARAMETRIZED = Map.ofEntries(
        Map.entry("products.byId",
            "//product[@id=$id]"),
        Map.entry("stocks.belowThreshold",
            "//stock[@quantity < $threshold]"),
        Map.entry("suppliers.byCountry",
            "//supplier[address/country=$country]"),
        Map.entry("products.bySkuPrefix",
            "//product[starts-with(sku, $prefix)]"),
        Map.entry("suppliers.ofProduct",
            "//supplier[@id = //product_supplier[@product_id=$id]/@supplier_id]"),
        Map.entry("products.atLocation",
            "//product[@id = //stock[@location_id=$id]/@product_id]"),
        Map.entry("offers.cheapestForProduct",
            "//product_supplier[@product_id=$id]"
                + "[not(price > //product_supplier[@product_id=$id]/price)]"),
        Map.entry("products.inCategory",
            "//product[@category_id = //category[name=$name]/@id]"));

    private Expressions() {
    }
}
