package io.github.wxqdb_backend.function;

import java.io.File;

public class ShowDatabases {
    //show databases
    public static void showDatabase() {
        File file = new File("./mydatabase");
        File[] files = file.listFiles();
        if (files != null) {
            for (File value : files) {
                System.out.println(value.getName());
            }
        }
    }

    //将数据库名字存入String中return
    public static String showDatabaseWithReturn() {
        File file = new File("./mydatabase");
        File[] files = file.listFiles();
        //将所有数据库的名字存入一个String中,跳过xml结尾格式文件
        StringBuilder result = new StringBuilder();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".xml")) {
                    continue;
                }
                result.append(files[i].getName());
                if (i != 0 && i != files.length - 1) {
                    result.append("\n");
                }
            }
        }
        return result.toString();
    }
}
