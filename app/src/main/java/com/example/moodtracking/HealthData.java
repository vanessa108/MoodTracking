//TODO clean export data from unnecessary stuff to speed up the app
//TODO genrate data for upcoming months based on current data
//TODO import mood data
//TODO build a model that get minutes of activity by day
package com.example.moodtracking;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    Document xmlDocument_ext = null;
    Document xmlDocument_mod = null;
    XPath xPath = XPathFactory.newInstance().newXPath();
    List<extData> lastDaysSleep = new ArrayList<extData>();
    List<extData> lastDaysActivity = new ArrayList<extData>();
    List<extData> lastDaysStepActivity = new ArrayList<extData>();
    List<extData> lastDaysMood = new ArrayList<extData>();

    /*constuctor of the class HealthData*/
    public HealthData(InputStream ext,InputStream mod) {
        this.xmlDocument_ext = readXMLToDocument(ext);
        this.xmlDocument_mod = readXMLToDocument(mod);
    }
    private Document readXMLToDocument(InputStream x_is){
        Document xmlDocument = null;
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
            xmlDocument = builder.parse(x_is);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xmlDocument;
    }
    //function to get sleep data of the last x days
    public List<extData> getSleepData(int lastXDays) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {
        for (int i = 0; i < lastXDays; i++) {
            String Date = getDayMinusXasString(i);
            String expression = "//*[(@type = 'HKCategoryTypeIdentifierSleepAnalysis') and (number(translate(substring(@endDate, 0,11),'-',''))="+Date+")]";
            NodeList nl = (NodeList) xPath.compile(expression).evaluate(xmlDocument_ext, XPathConstants.NODESET);
            Log.d("len(nodelist",String.valueOf(nl.getLength()));
            if(nl.getLength()>0) {
                if (nl.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    lastDaysSleep.add(new SleepData(nodeListTOList(nl)));
                }
            }
            else{
                lastDaysSleep.add(new SleepData(null));
            }
        }
        return lastDaysSleep;
    }


     /*

        //TODO working with date comparison instead of using the last x days
        // xpath expresstion /Record[number(translate(substring(/Record/@startDate, 0,11),'-',''))>20160520]
        String expression = "/HealthData/Record[(@type = 'HKCategoryTypeIdentifierSleepAnalysis') and position()>last()-" + lastXDays + "]";
        NodeList nl = (NodeList) xPath.compile(expression).evaluate(xmlDocument_ext, XPathConstants.NODESET);
        //return Integer.toString(nodeList.getLength());
        int length = nl.getLength();
        for (int i = 0; i < length; i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                lastDaysSleep.add(nodeToSleepDataObj(nl.item(i)));
            }
        }
        */


    public List<extData> getStepData(int lastXDays) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {
        for (int i = 0; i < lastXDays; i++) {
            String Date = getDayMinusXasString(i);
            String expression = "/HealthData/Record[(@type = 'HKQuantityTypeIdentifierStepCount') and number(translate(substring(/HealthData/Record/@startDate, 0,11),'-',''))="+Date+"]";
            NodeList nl = (NodeList) xPath.compile(expression).evaluate(xmlDocument_ext, XPathConstants.NODESET);
            if(nl.getLength()>0) {
                if (nl.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    lastDaysStepActivity.add(new StepActivityData(nodeListTOList(nl)));
                }
            }
            else{
                lastDaysStepActivity.add(new StepActivityData(null));
            }
        }
        return lastDaysActivity;                                                                                                                   }
     
    public List<extData> getMoodData(int lastXDays) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {
        for (int i = 0; i < lastXDays; i++) {
            String Date = getDayMinusXasString(i);
            Log.d("Date",Date);
            String expression = "//*[number(translate(substring(@DateTime, 0,11),'-',''))="+Date+"]";
            NodeList nl = (NodeList) xPath.compile(expression).evaluate(xmlDocument_mod, XPathConstants.NODESET);
            if(nl.getLength()>0) {
                if (nl.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    lastDaysMood.add(new MoodData(nodeListTOList(nl)));
                }
            }
            else{
                lastDaysMood.add(new MoodData(null));
            }
        }
        return lastDaysMood;
    }


    private List<Node> nodeListTOList(NodeList nl){
        List<Node> nodes = new ArrayList<>();
        int length = nl.getLength();
        for (int i = 0; i < length; i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                nodes.add(nl.item(i));
            }
        }
        return nodes;
    }

    /*Help functions*/
    private Date dayMinusX(int x) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -x);
        return cal.getTime();
    }
    private String getDayMinusXasString(int x) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(dayMinusX(x));
    }
    private String nodeToString(Node node) {
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
}
















