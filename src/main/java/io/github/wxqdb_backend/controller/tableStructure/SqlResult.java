package io.github.wxqdb_backend.controller.tableStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlResult
{//返回类型
    public boolean ifMessage;
    public String message;

    public List<String> columns;

    public List<Map<String, String>> data;



    public SqlResult(String message)
    {
        ifMessage = true;
        message = message;
        columns =new ArrayList<>();
        data =new ArrayList<>();
    }
    public SqlResult(List<String> columns, List<Map<String, String>> data)
    {
        ifMessage = false;
        this.columns = columns;
        this.data = data;
    }
}
