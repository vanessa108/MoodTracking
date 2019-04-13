package com.example.moodtracking;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class SleepData {
    private String sleepAmount;
    private Date startDate = null;
    private Date endDate = null;
    public SleepData(String xml) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        this.parseXML(xml);
        this.calcSleepTime(this.startDate,this.endDate);

    }
    private void parseXML(String xml) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        InputSource source = new InputSource(new StringReader(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(source);

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        Log.d("SleepDataTest",xml+"\n");

        String date1 = xpath.evaluate("/Record/@startDate", document);
        String date2 = xpath.evaluate("/Record/@endDate", document);
        Log.d("SleepDataTest2",date1+"\n");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

        try {
            this.startDate = format.parse(date1);
            this.endDate = format.parse(date2);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    public String getSleepAmount(){
        return this.sleepAmount;
    }
    public String getStartDate(){
        String formattedDate = new SimpleDateFormat("dd-MM-yy").format(this.startDate);
        return formattedDate;
    }
    public String getEndDate(){
        String formattedDate = new SimpleDateFormat("dd-MM-yy").format(this.endDate);
        return formattedDate;
    }
    private void calcSleepTime(Date start,Date end){
        long diff = end.getTime() - start.getTime();
        /** remove the milliseconds part */
        diff = diff / 1000;
        long diffMinutes = diff / (60 ) % 60;
        long diffHours = diff / (60 * 60 );
        this.sleepAmount = Long.toString(diffHours)+"h "+Long.toString(diffMinutes)+"m";

    }
}
