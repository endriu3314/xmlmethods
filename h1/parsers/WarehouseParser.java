package h1.parsers;

import java.nio.file.Path;

import h1.models.Warehouse;

public interface WarehouseParser {
    Warehouse parse(Path file);
}
