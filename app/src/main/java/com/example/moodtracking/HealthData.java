package com.example.moodtracking;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import static javax.xml.parsers.DocumentBuilderFactory.*;

public class HealthData {
    NodeList nl = null;
    /*constuctor of the class HealthData*/
    public HealthData(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document doc = null;
        try {
            doc = builder.parse("../../assets/export.xml");
        } catch (IOException e){
            e.printStackTrace();
        } catch (SAXException e){
            e.printStackTrace();
        }

        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;
        try {
            expr = xpath.compile("//Type[@type_id=\"4218\"]");
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        try {
            this.nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

    }
    /*function to get sleep data of the last x days*/
    public void getSleepData(int lastXDays){

    }
    public void getStepData(){

    }
    public void getActivityData(){

    }
    public void combineStepsActivity(){

    }
    public NodeList getNodeList() {
        if (this.nl == null) {
            return this.nl;
        } else {
            return this.nl;
        }
    }

}
