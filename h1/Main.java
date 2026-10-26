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
        
    }
}
