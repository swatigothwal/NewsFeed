package com.example.NewsFeed;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public  class  NewsData {
    public static String dataat0;
    public static boolean checkApi() throws IOException, XmlPullParserException {
        ArrayList<RssItem> rssItemList1 = null;
        rssItemList1 = parseRSS("https://www.rte.ie/feeds/rss/?index=/news/&limit=20");
        if(rssItemList1.get(0).getTitle()==dataat0){
             return true;
        }
        return false;
    }

    private static ArrayList<RssItem> parseRSS(String urlString) throws IOException, XmlPullParserException {

        XmlPullParser parser = Xml.newPullParser();

        // create URL object from String
        URL feedURL = new URL(urlString);

        // create InputStream from URL
        InputStream inputStream = feedURL.openStream();

        // set XMLPullParser to use the input stream
        parser.setInput(inputStream, null);

        int eventType = parser.getEventType();

        boolean done = false;

        RssItem currentRSSItem = null;

        ArrayList<RssItem> resultRssList1 = new ArrayList<>();

        //ArrayList<RssItem> resultRssList1;
        while (eventType != XmlPullParser.END_DOCUMENT && !done) {
            String name = null;
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("item")) {
                        // a new item element
                        currentRSSItem = new RssItem();
                    } else if (currentRSSItem != null) {
                        if (name.equalsIgnoreCase("link")) {
                            currentRSSItem.setLink(parser.nextText());
                        } else if (name.equalsIgnoreCase("description")) {
                            currentRSSItem.setDescription(parser.nextText());
                        } else if (name.equalsIgnoreCase("pubDate")) {
                            currentRSSItem.setPubDate(parser.nextText());
                        } else if (name.equalsIgnoreCase("title")) {
                            currentRSSItem.setTitle(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("item") && currentRSSItem != null) {
                        resultRssList1.add(currentRSSItem);
                    } else if (name.equalsIgnoreCase("channel")) {
                        done = true;
                    }
                    break;
            }
            eventType = parser.next();
        }

        return resultRssList1;
    }
}
