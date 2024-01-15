package io.github.wxqdb_backend.function;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;

public class Help {
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

    public static String read_helpWithReturn() throws DocumentException {//读取帮助文件
        File file = new File("./help.xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        List<Node> nodes = document.getRootElement().selectNodes("help");
        String str = "";
        for (Node node : nodes) {
            Element element = (Element) node;
            str += element.getText() + "\n";
        }
        return str;
    }
}
