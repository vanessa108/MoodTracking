package com.example.moodtracking;

import android.util.Log;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public abstract class extData {
    String xml;
    Date startDate;
    Date endDate;
    Document document;
    public extData(String XML) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        xml = XML;
        this.parseXML(xml);
    }

    public void parseXML(String xml) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
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
    }
    public abstract String getValue();
    public abstract void calcValue();
    public String getStartDate(){
        String formattedDate = new SimpleDateFormat("dd-MM-yy").format(startDate);
        return formattedDate;
    }
    public String getEndDate(){
        String formattedDate = new SimpleDateFormat("dd-MM-yy").format(endDate);
        return formattedDate;
    }
}
class SleepData extends extData{
    private String sleepAmount;
    public SleepData(String xml) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        super(xml);
        calcValue();
    }
    public void calcValue(){
        long diff = endDate.getTime() - startDate.getTime();
        /** remove the milliseconds part */
        diff = diff / 1000;
        long diffMinutes = diff / (60 ) % 60;
        long diffHours = diff / (60 * 60 );
        sleepAmount = Long.toString(diffHours)+"h "+Long.toString(diffMinutes)+"m";

    }
    public String getValue(){
        return sleepAmount;
    }
}
class ActivityData extends extData{
    int value;
    public ActivityData(String xml) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        super(xml);
    }
    public String getValue(){
        return "Activity";
    }
    public void calcValue(){
        this.value = 1;
    }

}

