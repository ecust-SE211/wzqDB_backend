package io.github.wxqdb_backend.function;

import com.alibaba.fastjson.JSON;
import io.github.wxqdb_backend.bpulstree.BPlusTree;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.github.wxqdb_backend.controller.tableStructure.UpdateData;

public class UpdateDataFromTable {
    public static void updateTable(String dbName, String tbName, List<List<String>> tmp) throws DocumentException, IOException {
        //数据库是否为空
        if (IsLegal.isDatabaseEmpty()) {
            return;
        }
        //表存在则返回表的物理层最后一张子表的下标,得到配置文件
        File config_file = IsLegal.isTable(dbName, tbName);
        //find标记是否找到记录
        Boolean find = false;
        //tmp2表示where的列名称和列值
        String[] tmp2 = tmp.get(1).get(0).split("=");
        //key为where的列名称
        String key = tmp2[0];
        //是否是主键查询
        if (IsLegal.isIndex(config_file, key)) {
            BPlusTree tree = CreateIndex.findTree(tbName);
            String file_name = tree.search(Integer.parseInt(key));
            File file = new File("./mydatabase/" + dbName + "/" + tbName + "/" + file_name + ".xml");
            find = update(tbName, file, tmp, tmp2);
            if (!find) {
                System.out.println("更新失败，未找到记录");
            }
            //更新索引文件
//            CreateIndex.updateIndex_update(tbName,key,tmp2[2]);
        } else {
            //扫描所有文件,j记录文件下表,num用来遍历所有文件
            String this_file = IsLegal.lastFileName(dbName, tbName);
            for (int j = Integer.parseInt(this_file); j >= 0; j--) {
                String num = "" + j;
                File file = new File("./mydatabase/" + dbName + "/" + tbName + "/" + tbName + num + ".xml");
                find = update(tbName, file, tmp, tmp2);
                if (find) {
                    return;
                }
            }
            System.out.println("更新失败，未找到记录");
        }
    }

    public static String updateTableWithReturn(String dbName, String tbName, List<List<String>> tmp) throws DocumentException, IOException {
        //数据库是否为空
        if (IsLegal.isDatabaseEmpty()) {
            return "数据库不存在";
        }
        //表存在则返回表的物理层最后一张子表的下标,得到配置文件
        File config_file = IsLegal.isTable(dbName, tbName);
        //find标记是否找到记录
        Boolean find = false;
        //tmp2表示where的列名称和列值
        String[] tmp2 = tmp.get(1).get(0).split("=");
        //key为where的列名称
        String key = tmp2[0];
        //是否是主键查询
        if (IsLegal.isIndex(config_file, key)) {
            BPlusTree tree = CreateIndex.findTree(tbName);
            String file_name = tree.search(Integer.parseInt(key));
            File file = new File("./mydatabase/" + dbName + "/" + tbName + "/" + file_name + ".xml");
            find = update(tbName, file, tmp, tmp2);
            if (!find) {
                return "更新失败，未找到记录";
            }
        } else {
            //扫描所有文件,j记录文件下表,num用来遍历所有文件
            String this_file = IsLegal.lastFileName(dbName, tbName);
            for (int j = Integer.parseInt(this_file); j >= 0; j--) {
                String num = "" + j;
                File file = new File("./mydatabase/" + dbName + "/" + tbName + "/" + tbName + num + ".xml");
                find = update(tbName, file, tmp, tmp2);
                if (find) {
                    return "更新成功";
                }
            }
        }
        return "更新失败，未找到记录";
    }

    public static boolean updateSingleData(UpdateData data) throws DocumentException, IOException {
        //更改单条数据
        SAXReader reader = new SAXReader();
        File file = new File("./mydatabase/" + data.dbName + "/" + data.tbName + "/" + data.tbName + "0.xml");
        Document document = reader.read(file);
        Element root = document.getRootElement();//获取根节点
        List<Node> nodes = root.selectNodes(data.tbName);
        System.out.println(JSON.toJSONString(data));
        int count = 0;//第几个node的计数器
        for (Node node : nodes) {

            if (count == data.updateIndex) {
                Element element = (Element) node;
                List<Attribute> list = element.attributes();
                for (Attribute attribute : list) {//更新xml中每一条数据的值
                    if (data.updateKeyValue.containsKey(attribute.getName())) {
                        attribute.setText(data.updateKeyValue.get(attribute.getName()));
                    }
                }
            }
            count++;
        }
        CreateTable.writeIO(file, document);
        return true;
    }


    public static boolean update(String tbName, File file, List<List<String>> tmp, String[] tmp2) throws DocumentException, IOException {
        //创建解析器，document对象，获得根节点
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element root = document.getRootElement();
        int unFindNum = 0;//未找到记录的次数
        List<Node> nodes = root.selectNodes(tbName);
        for (Node node : nodes) {
            boolean find = false;
            Element currentNode = (Element) node;
            List<Attribute> list = currentNode.attributes();
            for (Attribute attribute : list) {
                if (attribute.getName().equals(tmp2[0]) && attribute.getText().equals(tmp2[1])) {
                    find = true;
                    break;
                }
            }
            if (find) {
                for (int k = 0; k < tmp.get(0).size(); k++) {
                    String[] tmp1 = tmp.get(0).get(k).split("=");
                    for (Attribute attribute : list) {
                        if (attribute.getName().equals(tmp1[0])) {
                            attribute.setText(tmp1[1]);
                        }
                    }
                }
                //写入IO
                CreateTable.writeIO(file, document);
                System.out.println("更新成功");
                unFindNum++;
            }
        }
        if (unFindNum == nodes.size()) {
            return false;
        } else {
            return true;
        }

    }
}
