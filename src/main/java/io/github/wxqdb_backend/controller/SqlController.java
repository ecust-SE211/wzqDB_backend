package io.github.wxqdb_backend.controller;

import io.github.wxqdb_backend.controller.dto.SqlDto;
import io.github.wxqdb_backend.factory.PassingParametersFactory;
import io.github.wxqdb_backend.factory.SingleSqlParserFactory;
import io.github.wxqdb_backend.function.SelectDataFromTable;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.*;
import io.github.wxqdb_backend.function.Help;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sql")
public class SqlController {

    @PostMapping("/execute")
    public String execute(@RequestBody SqlDto sqlDto) throws DocumentException, IOException {
        //System.out.println("请输入SQL语句：（您可以输入help以查询SQL语句帮助）");
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
        String sql = sqlDto.getSql();
        System.out.println("sql:" + sql);
        if (sql.equals("help")) {
            Help.read_helpWithReturn();
        }

        if (sql.lastIndexOf(";") != sql.length() - 1) {
            //TODO:异常处理
            return "SQL语句末尾缺少分号";
        }

        sql = sql.trim(); // 去除字符串两侧的空白字符
        sql = sql.toLowerCase();//全部转为小写
        sql = sql.replaceAll("\\s+", " ");// 将sql中的连续空白字符替换为一个空格
        sql = sql.substring(0, sql.lastIndexOf(";"));// 去除SQL语句末尾的分号
        sql = "" + sql + " ENDOFSQL";
        System.out.println("1)SQL预处理结果: " + sql);
        List<List<String>> parameter_list = new ArrayList<List<String>>();//缓存sql body信息的list
        //将预处理后的SQL语句匹配SQL正则表达式，返回含有SQL的body信息的List
        try {
            parameter_list = SingleSqlParserFactory.generateParser(sql);
        } catch (Exception e) {
            e.printStackTrace();//异常处理，不用管
        }
        //根据SQL的body部分，调用相应的功能模块
        return PassingParametersFactory.dealParametersWithReturn(parameter_list);
    }

    @GetMapping("/{DbName}/{TbName}")
    public List<Map<String, String>> GetTableData(@PathVariable String DbName, @PathVariable String TbName) throws DocumentException, IOException
    {
        return null;//SelectDataFromTable.
    }

}
