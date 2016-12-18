package controller.tasks;

import controller.WikiController;
import model.business.DataBusiness;
import model.business.UnitBusiness;
import model.pojo.Data;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class UpdateCharacterList extends Thread {
    @Override
    public void run() {
        try {
            WikiController wikia = new WikiController();
            DataBusiness dataBusiness = new DataBusiness("wiki");
            UnitBusiness unitBusiness = new UnitBusiness("wiki");
            Data lastDateCheck = dataBusiness.getByName("Last character check");
            String lastCheck = null;
            if(lastDateCheck != null) {
                lastCheck = wikia.formatDateForWiki(lastDateCheck.getDate());
            }
            else {
                lastDateCheck = new Data();
                lastDateCheck.setName("Last character check");
                lastDateCheck.setDate(LocalDateTime.now());
                dataBusiness.add(lastDateCheck);
            }
            Document doc = wikia.consult("action=query&list=categorymembers&cmtitle=Category:Units&cmlimit=500&cmsort=timestamp&cmdir=desc" +
                    (lastCheck != null ? "&cmend=" + lastCheck : ""));
            do {
                NodeList units = doc.getElementsByTagName("cm");
                for(int i = 0; i < units.getLength(); i++) {
                    if(unitBusiness.getById(units.item(i).getAttributes().getNamedItem("pageid").getTextContent()) == null) {
                        unitBusiness.add(unitBusiness.createFromWikia(units.item(i).getAttributes().getNamedItem("pageid").getTextContent()));
                    }
                    else {
                        unitBusiness.update(unitBusiness.createFromWikia(units.item(i).getAttributes().getNamedItem("pageid").getTextContent()));
                    }
                }
                doc = wikia.queryContinue();
            } while (doc != null);
            lastDateCheck.setDate(LocalDateTime.now());
            dataBusiness.update(lastDateCheck);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
