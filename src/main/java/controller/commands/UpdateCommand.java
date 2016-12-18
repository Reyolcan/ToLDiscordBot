package controller.commands;

import controller.ErrorController;
import controller.tasks.UpdateCharacterList;
import model.business.UserBusiness;
import model.pojo.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.sql.SQLException;

public class UpdateCommand implements Command {
    private final String HELP = "$$update - Updates from the wiki";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        try {
            if(args.length == 0) {
                UserBusiness userBusiness = new UserBusiness(event.getGuild().getId());
                User user = userBusiness.getById(event.getAuthor().getId());
                if(user.isBoss()) {
                    return true;
                }
                else {
                    event.getChannel().sendMessage(":anger: You don't have rights to use this command");
                }
            }
        } catch (SQLException e) {
            ErrorController.logStackTrace(e);
        }
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        new UpdateCharacterList().start();
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
