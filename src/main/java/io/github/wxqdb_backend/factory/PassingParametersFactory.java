package io.github.wxqdb_backend.factory;

import io.github.wxqdb_backend.controller.tableStructure.SqlResult;
import io.github.wxqdb_backend.function.*;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * 工厂类：对解析后的sql语句进行判断，调用相应功能的模块函数
 */
public class PassingParametersFactory {

    public static void dealParameters(List<List<String>> list) throws IOException, DocumentException {//将语句预处理后，生成的结果
        List<String> ls = new ArrayList<String>();
        ls = list.get(0);
//--------------------------------------------------------------------------------------------------------------------------------------
        String sql_key = ls.get(0);//一开始的肯定是处理语句

        if (sql_key.equals("create table")) {
            System.out.println("3)调用方法：创建表");

            List<String> bodyList = new ArrayList<String>();//传递参数
            List<String> body = list.get(1);
            for (int i = 1; i < body.size(); i++) {
                bodyList.add(body.get(i));
            }
            CreateTable.createTb(UseDatabase.dbName, ls.get(1), bodyList);

        } else if (sql_key.equals("show databases")) {
            System.out.println("3)调用方法：列出所有数据库");
            ShowDatabases.showDatabase();
        } else if (sql_key.equals("show tables")) {
            System.out.println("3)调用方法：列出所有表");
            ShowTables.showTable(UseDatabase.dbName);
        } else if (sql_key.equals("use database")) {
            System.out.println("3)调用方法：进入数据库");
            UseDatabase.dbName = ls.get(1);
            //if database illegal
            if (!IsLegal.isDatabase()) {
                UseDatabase.dbName = null;
                return;
            }

        } else if (sql_key.equals("create database")) {
            System.out.println("3)调用方法：创建数据库");
            CreateDatabase.createDB(ls.get(1));
        } else if (sql_key.equals("insert into")) {
            System.out.println("3)调用方法：插入数据到表");

            List<String> tmp2 = list.get(2);
            List<String> tmp1 = list.get(1);
            //
            InsertDataIntoTable.insertIntoTable(UseDatabase.dbName, ls.get(1), tmp1, tmp2);
        } else if (sql_key.equals("select * from")) {
            //包含where条件
            if (list.size() > 1) {
                System.out.println("3)调用方法：查询指定记录");
                if (ls.size() > 2) {//有多表
                    List<String> tableNames = new ArrayList<String>();
                    for (int i = 1; i < ls.size(); i++) {
                        tableNames.add(ls.get(i));
                    }
                    List<String> condition = list.get(1);
                    SelectDataFromTable.selectWithMultipleTable(UseDatabase.dbName, tableNames, null, condition);
                } else {
                    String tableName = list.get(0).get(1);
                    List<String> condition = list.get(1);
                    SelectDataFromTable.select(UseDatabase.dbName, tableName, null, condition);
                }


            } else {
                System.out.println("3)调用方法：查询所有记录");
                String tableName = list.get(0).get(1);
                SelectDataFromTable.select(UseDatabase.dbName, tableName, null, null);
            }

        } else if (sql_key.equals("select")) {
            System.out.println("3)调用方法：查询记录中的某些列");
            if (list.get(1).size() > 2) {
                List<String> tableNames = new ArrayList<String>();
                //[select, name, email]
                //[from, rjj, rjj1]
                //[where, rjj.id=rjj1.sid]示例的分块
                for (int i = 1; i < list.get(1).size(); i++) {
                    tableNames.add(list.get(1).get(i));
                }
                List<String> columns = list.get(0);//获取列名
                List<String> condition = list.get(2);
                SelectDataFromTable.selectWithMultipleTable(UseDatabase.dbName, tableNames, columns, condition);
            } else {
                List<String> columns = list.get(0);
                List<String> condition = list.get(2);
                String tableName = list.get(1).get(1);
                SelectDataFromTable.select(UseDatabase.dbName, tableName, columns, condition);
            }
        } else if (sql_key.equals("update")) {
            System.out.println("3)调用方法：更新指定记录");

            List<List<String>> tmp = getPararmeterList(list);
            UpdateDataFromTable.updateTable(UseDatabase.dbName, list.get(0).get(1), tmp);
        } else if (sql_key.equals("drop database")) {
            System.out.println("3)调用方法：删除数据库");
            DropDatabase.deleteDB((ls.get(1)));
        } else if (sql_key.equals("drop table")) {
            System.out.println("3)调用方法：删除表");
            DropTable.deleteTable(UseDatabase.dbName, ls.get(1));
        } else if (sql_key.equals("delete from")) {
            System.out.println("3)调用方法：删除指定记录");
            //取出每个list中的start部分，只传递后面的参数部分；
            List<String> conditions = list.get(1);
            List<String> conditionList = new ArrayList<String>();
            for (int i = 1; i < conditions.size(); i++) {
                String r = conditions.get(i);
                conditionList.add(r);
            }
            DeleteDataFromTable.deleteFromTable(UseDatabase.dbName, ls.get(1), conditionList);
        } else if (sql_key.equals("create index on")) {
            System.out.println("3)调用方法：创建索引");
            CreateIndex.createIndex(UseDatabase.dbName, list.get(0).get(1), list.get(1).get(1));
        } else if (sql_key.equals("drop index on")) {
            System.out.println("3)调用方法：删除索引");
            DropIndex.dropIndex(UseDatabase.dbName, list.get(0).get(1));
        } else if (sql_key.equals("create user")) {
            System.out.println("3)调用方法：创建新用户");
            CreateUser.createUser();
        }
    }

    /*
     * 当list参数为多行时，用于提取多行有效的参数。
     */
    protected static List<List<String>> getPararmeterList(List<List<String>> list) {
        List<List<String>> tmp1 = new ArrayList<List<String>>(); //传递参数
        List<String> tmp2;
        List<String> tmp3;
        for (int i = 1; i < list.size(); i++) {
            tmp2 = new ArrayList<String>();
            tmp3 = new ArrayList<String>();

            tmp2 = list.get(i);
            for (int j = 1; j < tmp2.size(); j++) {
                String r = tmp2.get(j);
                tmp3.add(r);
            }
            tmp1.add(tmp3);
        }
        return tmp1;
    }

    public static SqlResult dealParametersWithReturn(List<List<String>> list) throws IOException, DocumentException {//将语句预处理后，生成的结果
        List<String> ls = new ArrayList<String>();
        ls = list.get(0);//一开始的肯定是处理语句
//--------------------------------------------------------------------------------------------------------------------------------------
        String sql_key = ls.get(0);

        if (sql_key.equals("create table")) {
            System.out.println("3)调用方法：创建表");

            List<String> bodyList = new ArrayList<String>();//传递参数
            List<String> body = list.get(1);
            for (int i = 1; i < body.size(); i++) {
                bodyList.add(body.get(i));
            }
            String result = CreateTable.createTbWithReturn(UseDatabase.dbName, ls.get(1), bodyList);
            SqlResult sqlResult = new SqlResult(result);
            return sqlResult;
        } else if (sql_key.equals("show databases")) {
            System.out.println("3)调用方法：列出所有数据库");
            String result = ShowDatabases.showDatabaseWithReturn();
            return new SqlResult(result);
        } else if (sql_key.equals("show tables")) {
            System.out.println("3)调用方法：列出所有表");
            return new SqlResult(ShowTables.showTableWithReturn(UseDatabase.dbName));
        } else if (sql_key.equals("use database")) {
            System.out.println("3)调用方法：进入数据库");
            UseDatabase.dbName = ls.get(1);
            if (IsLegal.isDatabase()) {
                return new SqlResult("成功进入数据库" + UseDatabase.dbName);
            }
            UseDatabase.dbName = null;
            return new SqlResult("数据库不存在");

        } else if (sql_key.equals("create database")) {
            System.out.println("3)调用方法：创建数据库");
            return new SqlResult(CreateDatabase.createDBWithReturn(ls.get(1)));
        } else if (sql_key.equals("insert into")) {
            System.out.println("3)调用方法：插入数据到表");

            List<String> tmp2 = list.get(2);
            List<String> tmp1 = list.get(1);
            //
            return new SqlResult(InsertDataIntoTable.insertIntoTableWithreturn(UseDatabase.dbName, ls.get(1), tmp1, tmp2));//返回运行结果
        } else if (sql_key.equals("select * from")) {
            //包含where条件
            if (list.size() > 1) {
                System.out.println("3)调用方法：查询指定记录");
                if (ls.size() > 2) {//有多表
                    List<String> tableNames = new ArrayList<String>();
                    for (int i = 1; i < ls.size(); i++) {
                        tableNames.add(ls.get(i));
                    }
                    List<String> condition = list.get(1);
                    return SelectDataFromTable.selectWithMultipleTableWithReturn(UseDatabase.dbName, tableNames, null, condition);//返回值
                } else {
                    String tableName = list.get(0).get(1);
                    List<String> condition = list.get(1);
                    return SelectDataFromTable.selectWithReturn(UseDatabase.dbName, tableName, null, condition);
                }

            } else {
                System.out.println("3)调用方法：查询所有记录");
                String tableName = list.get(0).get(1);
                return SelectDataFromTable.selectWithReturn(UseDatabase.dbName, tableName, null, null);
            }

        } else if (sql_key.equals("select")) {

            System.out.println("3)调用方法：查询记录中的某些列");
            if (list.get(1).size() > 2) {
                List<String> tableNames = new ArrayList<String>();
                //[select, name, email]
                //[from, rjj, rjj1]
                //[where, rjj.id=rjj1.sid]示例的分块
                for (int i = 1; i < list.get(1).size(); i++) {
                    tableNames.add(list.get(1).get(i));
                }
                List<String> columns = list.get(0);//获取列名
                List<String> condition = list.get(2);
                return SelectDataFromTable.selectWithMultipleTableWithReturn(UseDatabase.dbName, tableNames, columns, condition);
            } else {
                List<String> columns = list.get(0);
                List<String> condition = list.get(2);
                String tableName = list.get(1).get(1);
                return SelectDataFromTable.selectWithReturn(UseDatabase.dbName, tableName, columns, condition);
            }

        } else if (sql_key.equals("update")) {
            System.out.println("3)调用方法：更新指定记录");

            List<List<String>> tmp = getPararmeterList(list);
            return new SqlResult(UpdateDataFromTable.updateTableWithReturn(UseDatabase.dbName, list.get(0).get(1), tmp));
        } else if (sql_key.equals("drop database")) {
            System.out.println("3)调用方法：删除数据库");
            return new SqlResult(DropDatabase.deleteDBWithReturn((ls.get(1))));
        } else if (sql_key.equals("drop table")) {
            System.out.println("3)调用方法：删除表");
            return new SqlResult(DropTable.deleteTableWithReturn(UseDatabase.dbName, ls.get(1)));
        } else if (sql_key.equals("delete from")) {
            System.out.println("3)调用方法：删除指定记录");
            //取出每个list中的start部分，只传递后面的参数部分；
            List<String> conditions = list.get(1);
            List<String> conditionList = new ArrayList<String>();
            for (int i = 1; i < conditions.size(); i++) {
                String r = conditions.get(i);
                conditionList.add(r);
            }
            return new SqlResult(DeleteDataFromTable.deleteFromTableWithReturn(UseDatabase.dbName, ls.get(1), conditionList));
        } else if (sql_key.equals("create index on")) {
            System.out.println("3)调用方法：创建索引");
            return new SqlResult(CreateIndex.createIndexWithReturn(UseDatabase.dbName, list.get(0).get(1), list.get(1).get(1)));
        } else if (sql_key.equals("drop index on")) {
            System.out.println("3)调用方法：删除索引");
            return new SqlResult(DropIndex.dropIndexWithReturn(UseDatabase.dbName, list.get(0).get(1)));
        }
        return new SqlResult("语句错误");
    }
}