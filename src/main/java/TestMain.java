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
        String test = "nyaca¡ dothis and this";
        System.out.print(test.replaceFirst("(.*)! ", ""));
    }
}
