package controller.commands;

import controller.ErrorController;
import model.CommandList;
import model.business.UserBusiness;
import model.pojo.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.sql.SQLException;

public class HelpCommand implements Command {
    private final String HELP = "What are you doing, you dummy?";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length <= 1) {
            return true;
        }
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        try {
            UserBusiness userBusiness = new UserBusiness(event.getGuild().getId());
            User user = userBusiness.getById(event.getAuthor().getId());
            if(args.length == 0) {
                String message = "```PoringBot's Command List\n----";
                for (CommandList command : CommandList.values()) {
                    if(!command.name().equals("HELP")) {
                        if(!command.isAdminOnly()) {
                            message += "\n$$" + command.name().toLowerCase() + "    " + command.getHelp();
                        }
                        else {
                            if(user.isBoss()) {
                                message += "\n$$" + command.name().toLowerCase() + "    " + command.getHelp();
                            }
                        }
                    }
                }
                message += "\n\nWrite $$help [command] for more information" +
                        "```";
                event.getChannel().sendMessage(message);
            }
            else {
                boolean found = false;
                for(CommandList command : CommandList.values()) {
                    if(command.name().equals(args[0].toUpperCase())) {
                        String[] help = command.getCommand().help().split("@@@");
                        String message = "```" + help[0];
                        if(help.length > 1) {
                            if(user.isBoss()) {
                                message += "\n" + help[1];
                            }
                        }
                        message += "```";
                        event.getChannel().sendMessage(message);
                        found = true;
                    }
                }
                if(!found) {
                    event.getChannel().sendMessage("That command doesn't exist!!");
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
