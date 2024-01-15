package io.github.wxqdb_backend.controller.tableStructure;

import java.util.ArrayList;
import java.util.List;

public class TbInfo {
    public String tableName;
    public List<TbColumn> columns;

    public TbInfo() {
        columns = new ArrayList<>();
    }
}
