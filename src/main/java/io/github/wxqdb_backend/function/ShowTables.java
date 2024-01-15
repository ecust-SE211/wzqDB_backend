package io.github.wxqdb_backend.function;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.github.wxqdb_backend.controller.tableStructure.TbColumn;
import io.github.wxqdb_backend.controller.tableStructure.TbInfo;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

public class ShowTables {
    //show tables
    public static void showTable(String dbname) {
        //数据库是否为空
        if (IsLegal.isDatabaseEmpty()) {
            return;
        }
        File dir = new File("./mydatabase/" + dbname + "");
        for (File file : dir.listFiles()) {
            if (file.exists()) {
                System.out.println(file.getName());
            }
        }
    }

    public static String showTableWithReturn(String dbname) {
        //数据库是否为空
        if (IsLegal.isDatabaseEmpty()) {
            return new String("数据库为空");
        }
        File dir = new File("./mydatabase/" + dbname + "");
        String result = new String("");
        for (File file : dir.listFiles()) {
            if (file.exists()) {
                result = result + file.getName();
                //两个之间加上换行符
                if (!result.equals("")) {
                    result = result + "\n";
                }

            }
        }
        return result;
    }

    //获取一个数据库下所有表的信息
    public static List<TbInfo> showTableWithStruct(String dbname) throws DocumentException {
        //数据库是否为空
        if (IsLegal.isDatabaseEmpty()) {
            return null;
        }
        List<TbInfo> tbList = new ArrayList<>();
        File dir = new File("./mydatabase/" + dbname);
        for (File file : Objects.requireNonNull(dir.listFiles())) {//循环遍历file
            if (!file.exists()) return null;
            TbInfo tempTable = new TbInfo();
            String tbName = file.getName();
            tempTable.tableName = tbName;
            File table = new File("./mydatabase/" + dbname + "/" + tbName + "/" + tbName + "-config.xml");
            if (table.exists()) {
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(table);
                Element node = document.getRootElement();
                List<TbColumn> tbColumns = new ArrayList<>();

                //遍历node中的attribute
                for (int i = 0; i < node.attributes().size(); i++) {
                    TbColumn tempColumn = new TbColumn();
                    Attribute attribute = node.attribute(i);
                    tempColumn.columnName = attribute.getName();
                    tempColumn.columnType = attribute.getValue();
                    tempColumn.isPrimaryKey = IsLegal.isIndex(table, attribute.getName());
                    tempTable.columns.add(tempColumn);
                }
            }
            tbList.add(tempTable);
        }
        return tbList;
    }
}
