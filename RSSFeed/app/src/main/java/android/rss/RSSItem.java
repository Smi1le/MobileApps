package android.rss;

import android.os.Parcelable;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.Locale;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RSSItem {

    private String title;
    private String description;
    private String pubDate;
    private String link;

    public RSSItem(String title, String description, String pubDate, String link) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getLink()
    {
        return this.link;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getPubDate()
    {
        return this.pubDate;
    }

    public String toString() {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy - hh:mm:ss");

        String result = getTitle() + "  ( " + sdf.format(this.getPubDate()) + " )";
        return result;
    }

    public static ArrayList<RSSItem> getRssItems(String feedUrl)
    {
        ArrayList<RSSItem> rssItems = new ArrayList<RSSItem>();
        try{
            URL url = new URL(feedUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream is = connection.getInputStream();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                Document document = db.parse(is);
                Element element = document.getDocumentElement();

                NodeList nodeList = element.getElementsByTagName("item");

                if (nodeList.getLength() > 0)
                {
                    for (int i = 0; i < nodeList.getLength(); i++)
                    {
                        Element newItem = (Element) nodeList.item(i);
                        Element titleE = (Element) newItem.getElementsByTagName("title").item(0);
                        Element descriptionE = (Element) newItem.getElementsByTagName("description").item(0);
                        Element pubDateE =  (Element) newItem.getElementsByTagName("pubDate").item(0);
                        Element linkE = (Element) newItem.getElementsByTagName("link").item(0);

                        String title = titleE.getFirstChild().getNodeValue();
                        String description = descriptionE.getChildNodes().item(1).getNodeValue();


                        String pubDate = pubDateE.getFirstChild().getNodeValue().substring(0, pubDateE.getFirstChild().getNodeValue().length() - 5);
                        String link = linkE.getFirstChild().getNodeValue();

                        RSSItem rssItem1 = new RSSItem(title, description, pubDate, link);

                        rssItems.add(rssItem1);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }


        return rssItems;
    }
}
