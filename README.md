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
