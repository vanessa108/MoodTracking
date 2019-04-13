package com.example.moodtracking;

import android.content.res.XmlResourceParser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class HealthData {
    Document xmlDocument = null;
    XPath xPath = XPathFactory.newInstance().newXPath();

    //constuctor of the class HealthData
    public HealthData(InputStream is) {
        DocumentBuilderFactory builderFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            //XmlResourceParser xpp = getResources().getXml(R.xml.export);
            this.xmlDocument = builder.parse(is);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //function to get sleep data of the last x days
    public NodeList getSleepData(int lastXDays) throws XPathExpressionException {
        //TODO working with date comparison instead of using the last x days
        String expression = "/HealthData/Record[(@type = 'HKCategoryTypeIdentifierSleepAnalysis') and position()>last()-" + lastXDays + "]";
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
        //return Integer.toString(nodeList.getLength());
        return nodeList;
    }

    public void getStepData() {
    //TODO needs to be implemented
    }

    public void getActivityData() {
    //TODO needs to be implemented
    }

    public void combineStepsActivity() {
    //TODO needs to be implemented
    }

    public String getExportDate() throws XPathExpressionException {
        String expression = "/HealthData/ExportDate/@value";
        //read an xml node using xpath
        Node node = (Node) this.xPath.compile(expression).evaluate(this.xmlDocument, XPathConstants.NODE);
        return nodeToString(node);
    }

    public String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        return sw.toString();
    }

    public SleepData nodeToSleepDataObj(Node node) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        return new SleepData(sw.toString());
    }
}
















