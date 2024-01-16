package io.github.wxqdb_backend.controller.tableStructure;

import java.util.HashMap;
import java.util.Map;

public class UpdateData {
    public String dbName;
    public String tbName;
    public int updataIndex;
    public Map<String, String> updateKeyValue;

    public UpdateData() {
        updataIndex = 0;
        updateKeyValue = new HashMap<String, String>();
    }
}
