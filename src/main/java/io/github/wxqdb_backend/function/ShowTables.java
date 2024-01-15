package io.github.wxqdb_backend.function;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.wxqdb_backend.controller.tableStructure.Table;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

public class ShowTables {
    //show tables
    public static void showTable(String dbname){
        //数据库是否为空
        if(IsLegal.isDatabaseEmpty()){
            return;
        }
        File dir=new File("./mydatabase/"+dbname+"");
        for(File file:dir.listFiles()){
            if(file.exists()){
                System.out.println(file.getName());
            }
        }
    }

    public static String showTableWithReturn(String dbname){
        //数据库是否为空
        if(IsLegal.isDatabaseEmpty()){
            return new String("数据库为空");
        }
        File dir=new File("./mydatabase/"+dbname+"");
        String result=new String("");
        for(File file:dir.listFiles()){
            if(file.exists()){
                result=result+file.getName();
                //两个之间加上换行符
                if(!result.equals("")){
                    result=result+"\n";
                }

            }
        }
        return result;
    }
    //获取一个数据库下所有表的信息
    public static List<Table> showTableWithStruct(String dbname) throws DocumentException {
        //数据库是否为空
        if(IsLegal.isDatabaseEmpty()){
            return null;
        }
        List<Table> tableList=new ArrayList<>();
        File dir=new File("./mydatabase/"+dbname+"");
        for(File file:dir.listFiles()){//循环遍历file
            if(file.exists()){
                Table tempTable=new Table();//初始化表
                tempTable.tbColumnName=new ArrayList<>();
                tempTable.tbColumnType=new ArrayList<>();
                tempTable.iftbColumnKey=new ArrayList<>();
                String tbName=file.getName();
                File table=new File("./mydatabase/"+dbname+"/"+tbName+"/"+tbName+"-config.xml");
                if(table.exists()){
                    SAXReader saxReader = new SAXReader();
                    Document document = saxReader.read(table);
                    Element node = document.getRootElement();
                    //遍历node中的attribute
                    for (int i = 0; i< node.attributes().size(); i++){
                        Attribute attribute = node.attribute(i);
                        tempTable.tbColumnName.add(attribute.getName());//赋值
                        tempTable.tbColumnType.add(attribute.getValue());
                        if(IsLegal.isIndex(table,attribute.getName())){
                            tempTable.iftbColumnKey.add(true);
                        }
                        else{
                            tempTable.iftbColumnKey.add(false);
                        }
                    }
                }
                tableList.add(tempTable);
            }
        }
        return tableList;
    }
}
