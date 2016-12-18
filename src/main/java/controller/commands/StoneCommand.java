package controller.commands;

import controller.ErrorController;
import model.business.UserBusiness;
import model.pojo.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.sql.SQLException;

public class StoneCommand implements Command{
    private final String HELP = "$$hs - Shows the amount of stones you have" +
            "\n$$hs @mention - Shows the amount of stones that user has" +
            "@@@" +
            "$$hs @mention give X - Gives that user X amount of stones" +
            "\n$$hs @mention take X - Takes X amount of stones away from that user";

    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length < 4) {
            if(args.length == 3) {
                for(int i = 0; i < args[2].length(); i++) {
                    int code = args[2].charAt(i);
                    if(code < 48 || code > 57) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void action(String[] args, MessageReceivedEvent event) {
        try {
            UserBusiness userBusiness = new UserBusiness(event.getGuild().getId());
            switch (args.length) {
                case 0:
                    event.getChannel().sendMessage(event.getAuthorName() + " has " + userBusiness.getById(event.getAuthor().getId()).getStones() + " Hero Stones");
                    break;
                case 1:
                    if (args[0].startsWith("@")) {
                        net.dv8tion.jda.entities.User user = event.getMessage().getMentionedUsers().get(0);
                        event.getChannel().sendMessage(user.getUsername() + " has " + userBusiness.getById(user.getId()).getStones() + " Hero Stones");
                    }
                    break;
            }
            if(args.length == 3) {
                User user = userBusiness.getById(event.getAuthor().getId());
                User targetUser = userBusiness.getById(event.getMessage().getMentionedUsers().get(0).getId());
                if(user.isBoss()) {
                    switch (args[1]) {
                        case "give":
                            targetUser.setStones(targetUser.getStones() + Integer.parseInt(args[2]));
                            if (userBusiness.update(targetUser) == 1) {
                                event.getChannel().sendMessage(event.getMessage().getMentionedUsers().get(0).getUsername() + " got " + args[2]
                                        + " new shiny stone" + (Integer.parseInt(args[2]) == 1 ? "" : "s"));
                            } else {
                                event.getChannel().sendMessage("There was some error processing that command :cry:");
                            }
                            break;
                        case "take":
                            targetUser.setStones(targetUser.getStones() - Integer.parseInt(args[2]));
                            if (userBusiness.update(targetUser) == 1) {
                                event.getChannel().sendMessage(event.getMessage().getMentionedUsers().get(0).getUsername() + " lost " + args[2]
                                        + " stone" + (Integer.parseInt(args[2]) == 1 ? "" : "s") + " to the void");
                            } else {
                                event.getChannel().sendMessage("There was some error processing that command :cry:");
                            }
                            break;
                    }
                }
                else {
                    event.getChannel().sendMessage(":anger: You don't have rights to use this command");
                }
            }
        } catch (SQLException e) {
            ErrorController.logStackTrace(e);
        }
    }

    public String help() {
        return HELP;
    }

    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
