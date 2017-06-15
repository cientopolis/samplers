package org.cientopolis.samplers;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Created by Xavier on 31/05/2017.
 */

public abstract class XMLManagement {


    private static String MANIFEST_FILE_NAME;
    private static String STRINGS_FILE_NAME;

    public static void setManifestFileName(String MANIFEST_FILE_NAME) {
        XMLManagement.MANIFEST_FILE_NAME = MANIFEST_FILE_NAME;
        System.out.println("Manifest:"+XMLManagement.MANIFEST_FILE_NAME);
    }


    public static void setStringsFileName(String STRINGS_FILE_NAME) {
        XMLManagement.STRINGS_FILE_NAME = STRINGS_FILE_NAME;
        System.out.println("Strings:"+XMLManagement.STRINGS_FILE_NAME);
    }

    private static Node findNode(Document doc, String tagName, String propertyName, String propertyValue) {
        Node nResult = null;

        NodeList nList = doc.getElementsByTagName(tagName);

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            Node tagNode = nNode.getAttributes().getNamedItem(propertyName);

            if (tagNode != null && tagNode.getNodeValue().equals(propertyValue)) {
                nResult = tagNode;
                break;
            }
        }

        return nResult;
    }


    public static void addMainActivity(String activityName, String activityLabel, String activityTheme) {
        try {

            Document doc = openXMLFile(MANIFEST_FILE_NAME);

            if (findNode(doc,"activity", "android:name", activityName) == null) {

                // get de Application node (there is only 1 node application)
                Node nApplication = doc.getElementsByTagName("application").item(0);

                // append a new node
                Element nActivity = doc.createElement("activity");

                Element nIntentFilter = doc.createElement("intent-filter");

                Element nIntentAction = doc.createElement("action");
                nIntentAction.setAttribute("android:name", "android.intent.action.MAIN");

                Element nIntentCategory = doc.createElement("category");
                nIntentCategory.setAttribute("android:name", "android.intent.category.LAUNCHER");

                nIntentFilter.appendChild(nIntentAction);
                nIntentFilter.appendChild(nIntentCategory);


                nActivity.setAttribute("android:name", activityName);
                nActivity.setAttribute("android:label", activityLabel);
                nActivity.setAttribute("android:theme", activityTheme);
                nActivity.appendChild(nIntentFilter);


                // append the activity node to the application node
                nApplication.appendChild(nActivity);
            }


            // save the changes
            saveXMLFile(doc, MANIFEST_FILE_NAME);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addGoogleMapsAPIKey(String googleMaps_API_KEY) {
        try {

            Document doc = openXMLFile(MANIFEST_FILE_NAME);

            if (findNode(doc,"meta-data", "android:name", "com.google.android.geo.API_KEY") == null) {

                // get de Application node (there is only 1 node application)
                Node nApplication = doc.getElementsByTagName("application").item(0);

                // append a new node
                Element nAPIKey= doc.createElement("meta-data");
                nAPIKey.setAttribute("android:name", "com.google.android.geo.API_KEY");
                nAPIKey.setAttribute("android:value", googleMaps_API_KEY);

                // append the activity node to the application node
                nApplication.appendChild(nAPIKey);
            }


            // save the changes
            saveXMLFile(doc, MANIFEST_FILE_NAME);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addString(String stringName, String stringValue) {
        try {

            Document doc = openXMLFile(STRINGS_FILE_NAME);

            if (findNode(doc,"string", "name", stringName) == null) {

                // get de resources node (there is only 1 node resources)
                Node nResources = doc.getElementsByTagName("resources").item(0);

                // append a new node
                Element nString = doc.createElement("string");
                nString.setAttribute("name", stringName);
                nString.setTextContent(stringValue);

                nResources.appendChild(nString);

            }


            // save the changes
            saveXMLFile(doc, STRINGS_FILE_NAME);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Document openXMLFile(String fileName) throws SAXException, IOException, ParserConfigurationException {

        File fXmlFile = new File(fileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        return doc;

    }

    private static void saveXMLFile(Document doc, String fileName) throws TransformerException {
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileName));
        transformer.transform(source, result);

        //System.out.println("saved");
    }
/*

    public static void main(String[] args){

        addMainActivity("MyMainSamplersActivity","@string/app_name", "@style/AppTheme");
        addString("app_name", "Prueba AAR");
        addString("otro", "algo de prueba");

        addGoogleMapsAPIKey("mi api key the google");

        System.out.println("done");


    }
*/
}
