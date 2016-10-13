package com.pnworangecake.aquariumplantxml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Plant {
    public String commonName;
    public String scientificName;
    public String image;
    public String description;
    public String type;
    public String placement;
    public String lighting;
    public String difficulty;
    public String tech;

    public Plant(
        String commonName,
        String scientificName,
        String image,
        String description,
        String type,
        String placement,
        String lighting,
        String difficulty,
        String tech
    ) {
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.image = image;
        this.description = description;
        this.type = type;
        this.placement = placement;
        this.lighting = lighting;
        this.difficulty = difficulty;
        this.tech = tech;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getPlacement() {
        return placement;
    }

    public String getLighting() {
        return lighting;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getTech() {
        return tech;
    }

    private static final String PLANT_XML = "plant";

    private static boolean isEnd(int eventType, String xppName, String endingKey) {
        return eventType == XmlPullParser.END_TAG && (xppName == null || xppName.equals(endingKey));
    }

    private static boolean isXMLText(int eventType, String previousKey, String key) {
        return (
            eventType == XmlPullParser.TEXT || eventType == XmlPullParser.START_TAG
        ) && previousKey.equals(key);
    }

    private static String getXMLText(
        XmlPullParser xpp,
        String startTag
    ) throws XmlPullParserException, IOException {
        StringBuilder sb = new StringBuilder();
        int eventType = xpp.getEventType();
        while (
            eventType != XmlPullParser.END_TAG || (xpp.getName() != null &&
            !xpp.getName().equals(startTag))
        ) {
            if (eventType == XmlPullParser.TEXT)
                sb.append(xpp.getText());
            else if (eventType == XmlPullParser.START_TAG && !xpp.getName().equals(startTag))
                sb.append(digXMLText(xpp, xpp.getName()));

            eventType = xpp.next();
        }
        return sb.toString();
    }

    private static String digXMLText(
        XmlPullParser xpp,
        String startTag
    ) throws XmlPullParserException, IOException {
        StringBuilder sb = new StringBuilder();
        int eventType = xpp.getEventType();
        sb.append('<').append(startTag).append('>');
        while (
            eventType != XmlPullParser.END_TAG || (
                xpp.getName() != null && !xpp.getName().equals(startTag)
            )
        ) {
            if (eventType == XmlPullParser.START_TAG && !xpp.getName().equals(startTag)) {
                // Dig into this.
                sb.append(digXMLText(xpp, xpp.getName()));
            } else if (eventType == XmlPullParser.TEXT) {
                sb.append(xpp.getText());
            }
            eventType = xpp.next();
        }
        sb.append("</").append(startTag).append('>');
        return sb.toString();
    }

    public static List<Plant> parseFile(Reader reader) {
        List<Plant> plants = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(reader);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && xpp.getName().equals(PLANT_XML))
                    plants.add(parseXML(xpp));

                eventType = xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return plants;
    }

    private static Plant parseXML(XmlPullParser xpp) throws XmlPullParserException, IOException {
        String previousKey = "", commonName = "", scientificName = "", image = "", description = "";
        String type = "", placement = "", lighting = "", difficulty = "", tech = "";
        int eventType = xpp.next();
        while (!isEnd(eventType, xpp.getName(), PLANT_XML)) {
            if (eventType == XmlPullParser.TEXT && previousKey.equals("common-name")) {
                commonName = xpp.getText().trim();
                previousKey = "";
            } else if (isXMLText(eventType, previousKey, "sci-name")) {
                scientificName = getXMLText(xpp, previousKey).trim();
                previousKey = "";
            } else if (eventType == XmlPullParser.TEXT && previousKey.equals("image")) {
                image = xpp.getText().trim();
                previousKey = "";
            } else if (eventType == XmlPullParser.TEXT && previousKey.equals("description")) {
                description = xpp.getText().trim();
                previousKey = "";
            } else if (eventType == XmlPullParser.TEXT && previousKey.equals("type")) {
                type = xpp.getText().trim();
                previousKey = "";
            } else if (eventType == XmlPullParser.TEXT && previousKey.equals("placement")) {
                placement = xpp.getText().trim();
                previousKey = "";
            } else if (eventType == XmlPullParser.TEXT && previousKey.equals("lighting")) {
                lighting = xpp.getText().trim();
                previousKey = "";
            } else if (eventType == XmlPullParser.TEXT && previousKey.equals("difficulty")) {
                difficulty = xpp.getText().trim();
                previousKey = "";
            } else if (eventType == XmlPullParser.TEXT && previousKey.equals("tech")) {
                tech = xpp.getText().trim();
                previousKey = "";
            } else if (eventType == XmlPullParser.START_TAG) {
                previousKey = xpp.getName();
            }
            eventType = xpp.next();
        }
        return new Plant(commonName, scientificName, image, description, type, placement, lighting, difficulty, tech);
    }
}
