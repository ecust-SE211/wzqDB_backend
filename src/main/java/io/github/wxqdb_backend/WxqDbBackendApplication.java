package io.github.wxqdb_backend;

import io.github.wxqdb_backend.function.CreateIndex;
import io.github.wxqdb_backend.function.IsLegal;
import io.github.wxqdb_backend.function.UseDatabase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import io.github.wxqdb_backend.factory.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
@SpringBootApplication
public class WxqDbBackendApplication {

    public static void main(String[] args) throws IOException, DocumentException
    {
        //SpringApplication.run(WxqDbBackendApplication.class, args);

        if (IsLegal.need_loadIndex()) {
            CreateIndex.loadIndex();
        }

        UseDatabase.dbName = "test";
        while (true) {
            System.out.println("请输入SQL语句：（您可以输入help以查询SQL语句帮助）");
            @SuppressWarnings("resource")
            Scanner input = new Scanner(System.in);
            String sql = input.nextLine();
//            /*
//             * 预处理:获得语句;
//             * 处理前后空格;
//             * 变小写;
//             * 处理中间多余的空格回车和特殊字符;
//             * 在末尾加特殊符号;
//             * 处理掉最后的;
//             */
//            //处理分行输入的问题，就是读;号才停止;
            //sql parse
            if (sql.equals("help")) {
                read_help();
                continue;
            }

            while (sql.lastIndexOf(";") != sql.length() - 1) {
                sql = sql + " " + input.nextLine();
            }

            sql = sql.trim(); // 去除字符串两侧的空白字符
            sql = sql.toLowerCase();//全部转为小写
            sql = sql.replaceAll("\\s+", " ");// 将sql中的连续空白字符替换为一个空格
            sql = sql.substring(0, sql.lastIndexOf(";"));// 去除SQL语句末尾的分号
            sql = "" + sql + " ENDOFSQL";
            System.out.println("1)SQL预处理结果: " + sql);

            List<List<String>> parameter_list = new ArrayList<List<String>>();//缓存sql body信息的list

            if (sql.equals("quit ENDOFSQL")) {
                return;
            } else {
                //将预处理后的SQL语句匹配SQL正则表达式，返回含有SQL的body信息的List
                try {
                    parameter_list = SingleSqlParserFactory.generateParser(sql);
                } catch (Exception e) {
                    e.printStackTrace();//异常处理，不用管

                }
                //根据SQL的body部分，调用相应的功能模块
                try {
                    PassingParametersFactory.dealParameters(parameter_list);
                } catch (Exception e) {
                    e.printStackTrace();//异常处理，不用管
                }
            }


        }

    }

    public static void read_help() throws DocumentException {//读取帮助文件
        File file = new File("./help.xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        List<Node> nodes = document.getRootElement().selectNodes("help");
        for (Node node : nodes) {
            Element element = (Element) node;
            System.out.println(element.getText());
        }
    }

}
