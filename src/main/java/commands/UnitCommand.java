package commands;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
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
public class UnitCommand implements Command {
    public final String HELP = "";

    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private DocumentBuilder db = null;

    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length == 1) {
            try {
                db = dbf.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Document category = null;
            try {
                category = db.parse(new URL("http://tales-of-link.wikia.com/api.php?action=query&titles=Category:" + args[0] + "&prop=categories&format=xml").openStream());
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            NodeList categNodes = category.getElementsByTagName("cl");
            if (categNodes.getLength() > 0) {
                for (int i = 0; i < categNodes.getLength(); i++) {
                    if(categNodes.item(i).getAttributes().getNamedItem("title").getTextContent().equals("Category:Character")) {
                        return true;
                    }
                }
            }
            event.getChannel().sendMessage("Who's that?!?! You trying to troll me?");
        }
        return false;
    }

    public void action(String[] args, MessageReceivedEvent event) {
        ArrayList<String> units = new ArrayList<String>();
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            Document doc = db.parse(new URL("http://tales-of-link.wikia.com/api.php?action=query&list=categorymembers&cmtype=page&cmtitle=Category:" + args[0] + "&cmlimit=20&format=xml").openStream());
            NodeList nodes = doc.getElementsByTagName("cm");
            for(int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                units.add(node.getAttributes().getNamedItem("title").getTextContent());
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String unitResponse = "";
        for(String unit:units) {
            unitResponse += unit + "\n";
        }
        unitResponse = unitResponse.replace("(", "[").replace(")", "]");
        event.getChannel().sendMessage(unitResponse);
    }

    public String help() {
        return HELP;
    }

    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
