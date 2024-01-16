package io.github.wxqdb_backend.factory;

import io.github.wxqdb_backend.sqlparser.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 工厂类:用于创建不同的BaseSingleSqlParser的实例，并对每句sql进行解析；
 */
public class SingleSqlParserFactory {
    public static List<List<String>> generateParser(String sql)//返回处理后的Sql
    {
        BaseSingleSqlParser tmp = null;
        /**
         * 在Java中，正则表达式 (use database)(.+) 的含义是匹配以下内容：
         * (use database)：这部分是一个捕获组（capture group），它会匹配文本 "use database" 确切内容。这里的括号 () 用于创建一个组，可以在后续操作中引用这个匹配到的子串。
         * (.+)：这部分也是一个捕获组，它代表任意一个或多个（即至少一个）任意字符（除了换行符，因为默认情况下.不匹配换行符）。+ 是量词，表示前面的元素需要重复一次或多次。
         * 所以整个正则表达式会查找以 "use database" 开始，并紧接着一个或多个任意字符的字符串。例如，在某个文本中，它可能会匹配如下的部分：
         */
        if (contains(sql, "(create database)(.+)")) {// 判断字符串sql是否包含"(create database)"以及之后的任意字符
            System.out.println("2)匹配正则表达式：create database");
            tmp = new CreateDatabaseSqlParser(sql);
        } else if (contains(sql, "(drop database)(.+)")) {
            System.out.println("2)匹配正则表达式：drop database");
            tmp = new DropDatabaseSqlParser(sql);

        } else if (contains(sql, "(show databases)")) {
            System.out.println("2)匹配正则表达式：show databases");
            tmp = new ShowDatabaseSqlParser(sql);
        } else if (contains(sql, "(show tables)")) {
            System.out.println("2)匹配正则表达式：show tables");
            tmp = new ShowTablesSqlParser(sql);
        } else if (contains(sql, "(use database)(.+)")) {//
            System.out.println("2)匹配正则表达式：use database");
            tmp = new UseDatabaseSqlParser(sql);
        } else if (contains(sql, "(create table)(.+)")) {
            System.out.println("2)匹配正则表达式：create table");
            tmp = new CreateTableSqlParser(sql);

        } else if (contains(sql, "(insert into)(.+)(values)(.+)")) {
            System.out.println("2)匹配正则表达式：insert into");
            tmp = new InsertSqlParser(sql);

        } else if (contains(sql, "(insert into)(.+)(valuess)(.+)()")) {
            System.out.println("2)匹配正则表达式：insert into where");
            tmp = new InsertSelectSqlParser(sql);

        } else if (contains(sql, "(select \\* from)(.+)")) {
            if (contains(sql, "(select \\* from)(.+)(where)(.+)")) {
                System.out.println("2)匹配正则表达式：select * from where");
                tmp = new SelectAllWhereSqlParser(sql);
            } else {
                System.out.println("2)匹配正则表达式：select * from");
                tmp = new SelectAllSqlParser(sql);
            }

        } else if (contains(sql, "(select)(.+)(from)(.+)")) {//自定义性最高查询语句

            System.out.println("2)匹配正则表达式：select from");
            tmp = new SelectSqlParser(sql);

        } else if (contains(sql, "(delete from)(.+)")) {
            System.out.println("2)匹配正则表达式：delete from");
            tmp = new DeleteSqlParser(sql);

        } else if (contains(sql, "(drop table)(.+)")) {
            System.out.println("2)匹配正则表达式：drop table");
            tmp = new DropTableSqlParser(sql);

        } else if (contains(sql, "(update)(.+)(set)(.+)")) {//TODO:更多复杂更新语句支持
            System.out.println("2)匹配正则表达式：update set");
            tmp = new UpdateSqlParser(sql);

        } else if (contains(sql, "(create index on)(.+)")) {
            System.out.println("2)匹配正则表达式：create index on");
            tmp = new CreateIndexSqlParser(sql);
        } else if (contains(sql, "(drop index on)(.+)")) {
            System.out.println("2)匹配正则表达式：drop index on");
            tmp = new DropIndexSqlParser(sql);
        } else if (contains(sql, "(create user)")) {
            System.out.println("2)匹配正则表达式：create user");
            tmp = new CreateUserSqlParser(sql);
        } else {
            System.out.println("SQL语句有误，请重新输入");
            return null;
        }
        return tmp.splitSql2Segment();//注意此处是个方法，返回的是一个List<List<String>>

    }

    /**
     * 　* 看word是否在lineText中存在，支持正则表达式
     * 　* @param sql:要解析的sql语句
     * 　* @param regExp:正则表达式
     * 　* @return
     * 这个函数通过运用正则表达式判断一个字符串是否包含另一个字符串。
     * 它接受两个参数，要解析的SQL语句和一个正则表达式。
     * 它使用Pattern类编译正则表达式，并在SQL语句中使用Matcher类进行匹配。
     * 如果匹配成功，则返回true，否则返回false。函数具有不区分大小写匹配的能力。
     *
     */

    private static boolean contains(String sql, String regExp) {
        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        return matcher.find();
    }
}
