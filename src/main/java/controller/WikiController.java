package controller;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiController {
    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private DocumentBuilder db = null;
    private String contQuery = "";
    private String lastQuery = "";

    public Document consult(String query) {
        lastQuery = "http://tales-of-link.wikia.com/api.php?" + query + "&format=xml";
        contQuery = "";
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            Document doc = db.parse(new URL(lastQuery).openStream());
            if(doc.getElementsByTagName("query-continue").getLength() > 0) {
                NamedNodeMap cont = doc.getElementsByTagName("query-continue").item(0).getChildNodes().item(0).getAttributes();
                contQuery = "&" + cont.item(0).getNodeName() + "=" + cont.item(0).getNodeValue();
            }
            else {
                contQuery = "DONE";
            }
            return doc;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Document queryContinue() {
        if(contQuery.equals("DONE")) {
            return null;
        }
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            Document doc = db.parse(new URL(lastQuery + contQuery).openStream());
            if(doc.getElementsByTagName("query-continue").getLength() > 0) {
                NamedNodeMap cont = doc.getElementsByTagName("query-continue").item(0).getChildNodes().item(0).getAttributes();
                contQuery = "&" + cont.item(0).getNodeName() + "=" + cont.item(0).getNodeValue();
            }
            else {
                contQuery = "DONE";
            }
            return doc;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String formatDateForWiki(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy'T'hh:mm:ss'Z'"));
    }

    public String getUnitData(String name, String data) {
        Pattern regex = Pattern.compile("[|][ ]*" + name + "[ ]*=[ ]*[^|]+[ ]*[\n]");
        Matcher match = regex.matcher(data);
        if(match.find()) {
            return match.group().split("=")[1].replaceAll("^[ ]*", "").replaceAll("[ ]*[\n]$", "");
        }
        return null;
    }
}
