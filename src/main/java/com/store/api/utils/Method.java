package com.store.api.utils;

import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mysql.jdbc.StringUtils;

public class Method {

    private final static Logger LOG = LoggerFactory.getLogger(Method.class);

    /**
	 * 
	 */
    public static Integer intVal(String s) {
        if (StringUtils.isNullOrEmpty(s)) {
            return null;
        }
        return Integer.valueOf(s);

    }

    public static Long longVal(String s) {
        if (StringUtils.isNullOrEmpty(s)) {
            return null;
        }
        return Long.valueOf(s);
    }

    public static Double doubleVal(String s) {
        if (StringUtils.isNullOrEmpty(s)) {
            return null;
        }
        return Double.valueOf(s);
    }

    /**
     * md5 加密
     * 
     * @param s
     * @return
     */
    public static String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取xml字符串 节点值
     * 
     * @param protocolXML
     *            xml字符串
     * @param element
     *            节点名称
     * @return
     */

    public static String parse(String protocolXML, String element) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(protocolXML)));

        Element root = doc.getDocumentElement();
        NodeList books = root.getChildNodes();
        if (books != null) {
            for (int i = 0; i < books.getLength(); i++) {
                Node book = books.item(i);
                if (element.equals(book.getNodeName())) {
                    return book.getFirstChild().getNodeValue();
                }
            }
        }
        return null;

    }
    
    
    public static void main(String[] args) {
		System.out.println();
		String thisToken = Method.MD5("sfc&2012#^%888"+"v_15527140516"+1);
		System.out.println(thisToken.equals("a6d90422ad1e11f420522200e14a3898"));
		//8be310e0dd64d991f0ea0da7004fb01e
		//8be310e0dd64d991f0ea0da7004fb01e
	}
}
