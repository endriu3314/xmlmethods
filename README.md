# Warehouse Management System

## Homework #1

### Problem Analysis

The chosen problem is a Warehouse Management System for a factory.

The purpose of the application is to manage the inventory of a factory, by products, suppliers, locations and available stock.

A product may be provided by multiple suppliers.

### Data Model

Database is XML, starting with `<warehouse>` element.

The database contains the following elements:
- `<suppliers>`
- `<locations>`
- `<categories>`
- `<products>`
- `<product_suppliers>`
- `<stocks>`

#### Suppliers
- `id` - unique identifier
- `name` - supplier name
- `address` - supplier address formed by street, city and country
- `phone` - supplier phone

#### Locations
- `id` - unique identifier
- `name` - location name
- `code` - location code
- `type` - location type (MAIN, IMPORT, EXPORT)

#### Categories
- `id` - unique identifier
- `name` - category name

#### Products
- `id` - unique identifier
- `name` - product name
- `category_id` - category identifier

#### Product Suppliers
- `product_id` - product identifier
- `supplier_id` - supplier identifier
- `price` - price

#### Stocks
- `product_id` - product identifier
- `location_id` - location identifier
- `quantity` - quantity


### Query

#### Query 1 - Stock per product

We can do a query to calculate the total stock for each product by summing up the quantity from the stocks table.

#### Query 2 - Low stock alert and proposal of cheapest supplier

We can do a query to identify products with low stock and propose the cheapest supplier for each product based on the price from the product_suppliers table.

### Future Usage

Inbound and outbound flow via location types: MAIN (inbound), IMPORT (inbound), EXPORT (outbound), watch stock go from locations in INBOUND to locations in MAIN (in production) and to EXPORT which is ready to ship.

Location planning, find where each product stocks are stored and if needed merge stocks from different locations to optimize storage space.

Supplier by country reports, to calculate risk of a country disruption and find if we have alternative suppliers for the same product.

#### Extending the WMS

The WMS can be extended to include more features such as:
- Tracking of movements
- Orders system
- Minimum quantity per product for alerts
- Status for stocks, reserving for production, damaged stocks

## Homework #2

`h2/` is the Level B XForms mini-application: a Vite + Tailwind frontend
using `@jinntec/fore` as the XForms processor. It renders the warehouse
document (products, stocks, supplier prices) from the BaseX REST endpoint
(`/rest/warehouse/warehouse.xml`, proxied via `vite.config.js`) and allows
editing products and stock.

```sh
cd h2 && npm run dev
```

## Homework #3

`h3/` extends `h2` (Fore + Vite + Tailwind) with:

- `bindings.html` — `fx-bind` demos on a
  stock-adjustment form backed by `public/adjustment.xml`, plus a low-stock
  table driven by `public/filters.xml`.
- `index.html` — improved Level B app: validated editable tables for
  products, stocks, supplier prices and locations, live stock totals,
  a low-stock filter, and `fx-submission` PUT saving back to BaseX.

The Vite proxy injects BaseX Basic auth (`admin:mypassword`) for `/rest/*`.

## Homework #4

### XML Schema

`data/warehouse.xsd` describes the database. All elements and data types are
declared at global scope and referenced by name (reusable, flat structure).
Links between the categories of information use `xs:ID` (primary keys) and
`xs:IDREF` / `xs:IDREFS` (foreign keys: `product/@category_id`,
`product_supplier/@product_id`, `/@supplier_id`, `stock/@product_id`,
`/@location_id`, `message/@product_ids`). Entity ids in `warehouse.xml` were
changed to NCName values (`s1`, `l1`, `c1`, `p1`, `ps1`, `st1`, `m1`) to
satisfy `xs:ID`.

The schema also defines `messages`/`message` at root level for future
homeworks (stock alerts, restock requests, shipment notices).

`data/warehouse.xml` links the schema via
`xsi:noNamespaceSchemaLocation="warehouse.xsd"`.

### Parsers with validation (`h4/`)

The H1 DOM and SAX parsers were copied to `h4/` and extended with
XML Schema validation, in both required flavours:

- **Automatic validation on load** — `DocumentBuilderFactory` /
  `SAXParserFactory` are given the compiled `Schema` (`setSchema`), so
  `parse()` validates while reading the document; schema errors are rethrown
  and abort the parse.
- **Validation at request** — `WarehouseParser.validate(Path)` runs a
  `javax.xml.validation.Validator` over a `DOMSource` / `SAXSource`.

`data/warehouse_invalid.xml` is a deliberately invalid copy (duplicate ids,
bad patterns, dangling `IDREF`s, negative price/quantity) used to prove
validation fails on bad documents.

Run from the repository root:

```sh
javac -d out h4/Main.java h4/models/*.java h4/parsers/*.java
java -cp out h4.Main
```
