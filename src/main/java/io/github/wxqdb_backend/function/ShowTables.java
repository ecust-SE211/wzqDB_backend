package io.github.wxqdb_backend.function;

import java.io.File;

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
}
