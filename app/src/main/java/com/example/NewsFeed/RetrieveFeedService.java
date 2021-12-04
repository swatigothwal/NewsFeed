package com.example.NewsFeed;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.util.Xml;

import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class RetrieveFeedService extends IntentService {

    public RetrieveFeedService() {
        super("RetrieveFeedService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    Log.d("IT472", "starting handle intent budu");
    //retrieve rss
    ArrayList<RssItem> rssItemList = null;
        try {
        rssItemList = parseRSS("https://www.rte.ie/feeds/rss/?index=/news/&limit=20");
    } catch (IOException e) {
        Log.e("IT472", "IOException");
        e.printStackTrace();
    } catch (XmlPullParserException e) {
        Log.e("IT472", "XMLPullParserException");
        e.printStackTrace();
    }

        if (rssItemList != null) {
        Log.d("IT472", "retrieved list size is " + rssItemList.size());
    } else {
        Log.d("IT472", "retrieved list is null");
    }

    //broadcast rss

    Intent broadcastIntent = new Intent();

        broadcastIntent.setAction("RSS_RETRIEVED");
        broadcastIntent.putExtra("stories", rssItemList);
    getBaseContext().sendBroadcast(broadcastIntent);


}

    private ArrayList<RssItem> parseRSS(String urlString) throws IOException, XmlPullParserException {

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

        ArrayList<RssItem> resultRssList = new ArrayList<>();

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
                        resultRssList.add(currentRSSItem);
                    } else if (name.equalsIgnoreCase("channel")) {
                        done = true;
                    }
                    break;
            }
            eventType = parser.next();
        }
        NewsData.dataat0 = resultRssList.get(0).getTitle();
        return resultRssList;
    }


}
