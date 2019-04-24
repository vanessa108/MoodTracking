package com.example.moodtracking;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
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
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public abstract class extData<T,K> {
    List<Node> nodes;
    String xml;
    Date startDate;
    Date endDate;
    Document document;
    public extData(List<Node> nodes) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        nodes = nodes;
    }
    public abstract T parseXML(String xml) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException;

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
    public abstract K getValue();
    public abstract void calcValue() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException;
    public String getStartDate(){
        String formattedDate = new SimpleDateFormat("dd-MM-yy").format(startDate);
        return formattedDate;
    }
    public String getEndDate(){
        String formattedDate = new SimpleDateFormat("dd-MM-yy").format(endDate);
        return formattedDate;
    }
}




class SleepData extends extData<Boolean,Long>{
    private long sleepAmount = 0;
    public SleepData(List<Node> nodes) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        super(nodes);
        if (nodes != null) {
            xml = super.nodeToString(nodes.get(0));
            parseXML(xml);
            calcValue();
        }
    }
    public Boolean parseXML(String xml) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        InputSource source = new InputSource(new StringReader(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        document = db.parse(source);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        String date1 = xpath.evaluate("/Record/@startDate", document);
        String date2 = xpath.evaluate("/Record/@endDate", document);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

        try {
            startDate = format.parse(date1);
            endDate = format.parse(date2);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return Boolean.TRUE;
    }

    public void calcValue(){
        sleepAmount = endDate.getTime() - startDate.getTime();

        /** remove the milliseconds part */
        //diff = diff / 1000;
        //long diffMinutes = diff / (60 ) % 60;
        //ong diffHours = diff / (60 * 60 );
        //sleepAmount = Long.toString(diffHours)+"h "+Long.toString(diffMinutes)+"m";

    }
    public Long getValue(){
        return sleepAmount;
    }
}


class StepActivityData extends extData<Long,String>{
    private String stepAmount;
    private String stepActivityMin;
    int stepamount=0;
    long activityTime = 0;


    public StepActivityData(List<Node> nodes) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        super(nodes);
        if(nodes!=null){
            calcValue();
        }
    }
    public Long parseXML(String xml) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        Date start = null;
        Date end = null;
        long diff = 0;

        InputSource source = new InputSource(new StringReader(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        document = db.parse(source);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();


        try {
            stepamount += Integer.parseInt(xpath.evaluate("/Record/@value",document));
        }
        catch (NumberFormatException e)
        {
            stepamount += 0;
        }
        String date1 = xpath.evaluate("/Record/@startDate", document);
        String date2 = xpath.evaluate("/Record/@endDate", document);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

        try {
            start = format.parse(date1);
            end = format.parse(date2);
            diff =calcDiff(start,end);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return diff;
    }
    private long calcDiff(Date start,Date end){
        long diff = end.getTime() - start.getTime();
        /** remove the milliseconds part */
        return diff / 1000;
    }
    public String getValue(){
        long diffMinutes = activityTime / (60 ) % 60;
        long diffHours = activityTime / (60 * 60 );
        return Long.toString(diffHours)+"h "+Long.toString(diffMinutes)+"m";

    }
    public void calcValue() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        for (int i = 0; i < nodes.size(); i++) {
            activityTime+=parseXML(super.nodeToString(nodes.get(i)));
        }
        Log.d("MIN",Long.toString(activityTime));
        Log.d("Count",Integer.toString(stepamount));
    }
}
class MoodData extends extData<String,String>{
    public MoodData(List<Node> nodes) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        super(nodes);
    }

    @Override
    public String parseXML(String xml) {
        return null;
    }

    public String getValue(){
       return "MoodData";
    }
    public void calcValue(){
    }
}

