import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Reyolcan on 29/08/2016.
 */
public class TestMain {

    public static void main(String[] args) {
        ArrayList<String> units = new ArrayList<String>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            Document doc = db.parse(new URL("http://tales-of-link.wikia.com/api.php?action=query&list=categorymembers&cmtitle=Category:Anise&cmlimit=100&format=xml").openStream());
            NodeList nodes = doc.getElementsByTagName("cm");
            for(int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if(node.getAttributes().getNamedItem("ns").getTextContent().equals("0")) {
                    units.add(node.getAttributes().getNamedItem("title").getTextContent());
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String unity = "";
        for(String unit:units) {
            unity += unit + "\n";
        }
        unity = unity.replace("(", "[").replace(")", "]");
        System.out.print(unity);
        System.out.print(unity);
    }
}
