package commands;

import database.DBConnect;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.List;

public class StoneCommand implements Command{
    public final String HELP = "";
    private boolean admin;

    public StoneCommand(boolean admin) {
        this.admin = admin;
    }

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
        DBConnect db = new DBConnect(event.getGuild().getId());
        if(!admin) {
            switch (args.length) {
                case 0:
                    event.getChannel().sendMessage(event.getAuthorName() + " has " + db.getStones(event.getAuthor().getId()) + " Hero Stones");
                    break;
                case 1:
                    if (args[0].startsWith("@")) {
                        User user = event.getMessage().getMentionedUsers().get(0);
                        event.getChannel().sendMessage(user.getUsername() + " has " + db.getStones(user.getId()) + " Hero Stones");
                    }
                    break;
            }
        }
        else {
            if(args.length == 3) {
                switch (args[1]) {
                    case "give":
                        if(db.setStones(Integer.parseInt(args[2]), event.getMessage().getMentionedUsers().get(0).getId())) {
                            event.getChannel().sendMessage(event.getMessage().getMentionedUsers().get(0).getUsername() + " got " + args[2]
                                    + " new shiny stone" + (Integer.parseInt(args[2]) == 1? "" : "s"));
                        }
                        else {
                            event.getChannel().sendMessage("There was some error processing that command :cry:");
                        }
                        break;
                    case "take":
                        if(db.setStones(Integer.parseInt(args[2]) * -1, event.getMessage().getMentionedUsers().get(0).getId())) {
                            event.getChannel().sendMessage(event.getMessage().getMentionedUsers().get(0).getUsername() + " got " + args[2]
                                    + " new shiny stone" + (Integer.parseInt(args[2]) == 1? "" : "s"));
                        }
                        else {
                            event.getChannel().sendMessage("There was some error processing that command :cry:");
                        }
                        break;
                }
            }
        }
        db.close();
    }

    public String help() {
        return HELP;
    }

    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
