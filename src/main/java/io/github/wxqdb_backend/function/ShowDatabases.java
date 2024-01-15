package io.github.wxqdb_backend.function;

import java.io.File;

public class ShowDatabases {
    //show databases
    public static void showDatabase(){
        File file=new File("./mydatabase");
        File[] files=file.listFiles();
        for(int i=0;i<files.length;i++){
            System.out.println(files[i].getName());
        }
    }
    //将数据库名字存入String中return
    public static String showDatabaseWithReturn(){
        File file=new File("./mydatabase");
        File[] files=file.listFiles();
        //将所有数据库的名字存入一个String中
        String result="";
        for(int i=0;i<files.length;i++){
            result+=files[i].getName();
            if(i!=0 || i!=files.length-1)
            {
                result+="\n";
            }
        }
        return result;
    }
}
