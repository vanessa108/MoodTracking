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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    List<Node> nodes = new ArrayList<>();
    String xml;
    Date startDate = null;
    Date endDate = null;
    Document document;
    public extData(List<Node> nodes) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        this.nodes = nodes;
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
    public Date getStartDate(){
        //String formattedDate = new SimpleDateFormat("dd-MM-yy").format(startDate);
        return startDate;
    }
    public Date getEndDate(){
        //String formattedDate = new SimpleDateFormat("dd-MM-yy").format(endDate);
        return endDate;
    }
    public long calcDiff(Date start,Date end){
        long diff = end.getTime() - start.getTime();
        diff = diff / 1000;
        long diffMinutes = diff/60;
        return diffMinutes;
    }
}
class SleepData extends extData<Long,Long>{
    private long sleepAmount = 0;
    public SleepData(List<Node> nodes) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        super(nodes);
        this.nodes = nodes;

        calcValue();
    }
    public void calcValue() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        //long diff = endDate.getTime() - startDate.getTime();
        //diff = diff / 1000;
        //long diffMinutes = diff / (60 );
        //sleepAmount = diffMinutes;
        for (int i = 0; i < nodes.size(); i++) {
            String temp = super.nodeToString(nodes.get(i));
            //Log.d("Node:",String.valueOf(temp));

            sleepAmount += parseXML(temp);
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

        String date1 = xpath.evaluate("/Record/@startDate", document);
        String date2 = xpath.evaluate("/Record/@endDate", document);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

        try {
            start = format.parse(date1);
            end = format.parse(date2);
            diff =super.calcDiff(start,end);
            if(super.startDate!=null){
                if(start.compareTo(super.startDate) < 0){
                    super.startDate=start;
                }
            }
            else{super.startDate=start;}
            if(super.endDate!=null){
                if(end.compareTo(super.endDate) > 0){
                    super.endDate=end;
                }
            }
            else{super.endDate=end;}

        } catch (Exception e) {
            e.printStackTrace();

        }
        return diff;
    }
    public Long getValue(){
        Log.d("SleepAmount",String.valueOf(sleepAmount));
        return sleepAmount;
    }
}
class StepActivityData extends extData<Long,Long>{
    private String stepAmount;
    private String stepActivityMin;
    private int stepamount=0;
    private long activityTime = 0;


    public StepActivityData(List<Node> nodes) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        super(nodes);
        this.nodes = nodes;
        calcValue();

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
            diff = super.calcDiff(start,end);

        } catch (Exception e) {
            e.printStackTrace();

        }
        Log.d("tempDiff",String.valueOf(diff));

        return diff;
    }

    public Long getValue(){
        Log.d("activityTime",String.valueOf(activityTime));
        return activityTime;
    }
    public void calcValue() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {


        for (int i = 0; i < nodes.size(); i++) {
            String temp = super.nodeToString(nodes.get(i));
           // Log.d("Node:",String.valueOf(temp));

            activityTime += parseXML(temp);
        }

    }
}
class MoodData extends extData<String,Integer>{
    int mood = 999;
    public MoodData(List<Node> nodes) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        super(nodes);
        this.nodes = nodes;
        if(nodes!=null){
            parseXML(super.nodeToString(nodes.get(0)));
        }
    }

    @Override
    public String parseXML(String xml) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        InputSource source = new InputSource(new StringReader(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        document = db.parse(source);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        String date1 = xpath.evaluate("/row/@DateTime", document);
        mood = Integer.parseInt(xpath.evaluate("/row/@Mood", document));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

        try {
            startDate = format.parse(date1);
            endDate = format.parse(date1);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }

    public Integer getValue(){
        return mood;
    }
    public void calcValue(){}
}

