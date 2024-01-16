package io.github.wxqdb_backend.function;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class DropDatabase {
    //delete database 数据库名称
    public static void deleteDB(String dbname) throws IOException {

            File file = new File("./mydatabase/" + dbname + "");
            if (!file.exists()) {
                System.out.println("database " + dbname + " is not exist");
                return;
            }
            //若数据库中有表存在，则提示用户
            if (file.listFiles().length > 0) {
                System.out.println("数据库" + dbname + "中有表存在，是否继续删除(Y/N)");

                Scanner scanner=new Scanner(System.in);
                String answer=scanner.next();
                if (answer.toUpperCase().equals("Y")) {

                    File[] files = file.listFiles();
                    for (int i=0;i<files.length;i++) {
                        File[] xmls=files[i].listFiles();
                        for (int j=0;j<xmls.length;j++) {
                            xmls[j].delete();
                        }
                        files[i].delete();
                    }
                    file.delete();
                    System.out.println("数据库" + dbname + "删除成功");
                } else {
                    return;
                }
            }
            //若数据库为空，直接删除
            else {
                file.delete();
                System.out.println("数据库" + dbname + "删除成功");
            }
        }

    public static String deleteDBWithReturn(String dbname) throws IOException {

        File file = new File("./mydatabase/" + dbname + "");
        if (!file.exists()) {
            return "数据库 "+ dbname + "不存在";
        }
        //若数据库中有表存在，则提示用户
        if (file.listFiles().length > 0) {


                File[] files = file.listFiles();
                for (int i=0;i<files.length;i++) {
                    files[i].delete();
                }
                file.delete();
        }
        //若数据库为空，直接删除
        else {
            file.delete();
        }
        return "数据库" + dbname + "删除成功";
    }

}
