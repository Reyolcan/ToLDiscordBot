package controller.commands;

import controller.ErrorController;
import model.business.UnitBusiness;
import model.business.UserBusiness;
import model.pojo.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import utils.Emojis;

import java.sql.SQLException;

public class GachaCommand implements Command {
    private final String HELP = "$$pull - Pulls for a single unit" +
            "\n$$pull multi - Pulls 10 units at once" +
            "\n" +
            "\n Reminder: right now you won't keep the units";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length == 0) {
            return true;
        }
        if(args.length == 1) {
            if(args[0].equals("multi")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        try {
            UserBusiness userBusiness = new UserBusiness(event.getGuild().getId());
            UnitBusiness unitBusiness = new UnitBusiness("wiki");
            User user = userBusiness.getById(event.getAuthor().getId());
            if(args.length == 0) {
                if (user.getStones() >= 5) {
                    user.setStones(user.getStones() - 5);
                    userBusiness.update(user);
                    int rnd = (int) Math.floor(Math.random() * 100);
                    /*if (rnd == 0) {
                        event.getChannel().sendMessage(event.getAuthorName() + " pulled **[5*]" + db.getRandomUnit("5", true) + "**");
                    }*/
                    if (rnd >= 0 && rnd <= 5) {
                        event.getChannel().sendMessage(event.getAuthorName() + " pulled: \n" + Emojis.get("5star", event) + " " + unitBusiness.selectRandomUnit(5).getName());
                    }
                    if (rnd >= 6 && rnd <= 35) {
                        event.getChannel().sendMessage(event.getAuthorName() + " pulled: \n" + Emojis.get("4star", event) + " " + unitBusiness.selectRandomUnit(4).getName());
                    }
                    if (rnd >= 36) {
                        event.getChannel().sendMessage(event.getAuthorName() + " pulled: \n" + Emojis.get("3star", event) + " " + unitBusiness.selectRandomUnit(3).getName());
                    }
                } else {
                    event.getChannel().sendMessage(":anger: You don't have enough Hero Stones");
                }
            }
            else {
                if (user.getStones() >= 50) {
                    user.setStones(user.getStones() - 50);
                    userBusiness.update(user);
                    int rnd;
                    String message = event.getAuthorName() + " pulled:";
                    for(int i = 0; i < 10; i++) {
                        rnd = (int) Math.floor(Math.random() * 100);
                        /*if (rnd == 0) {
                            message += "\n**[5☆]" + db.getRandomUnit("5", true) + "**";
                        }*/
                        if (rnd >= 0 && rnd <= 5) {
                            message += "\n" + Emojis.get("5star", event) + " " + unitBusiness.selectRandomUnit(5).getName();
                        }
                        if (rnd >= 6 && rnd <= 35) {
                            message += "\n" + Emojis.get("4star", event) + " " + unitBusiness.selectRandomUnit(4).getName();
                        }
                        if (rnd >= 36) {
                            message += "\n" + Emojis.get("3star", event) + " " + unitBusiness.selectRandomUnit(3).getName();
                        }
                    }
                    event.getChannel().sendMessage(message);
                } else {
                    event.getChannel().sendMessage(":anger: You don't have enough Hero Stones");
                }
            }
        } catch (SQLException e) {
            ErrorController.logStackTrace(e);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
