package commands;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class PictureCommand implements Command {
    public final String HELP = "";

    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    public void action(String[] args, MessageReceivedEvent event) {
        event.getChannel().sendMessage("http://i.imgur.com/QpJZy1P.png");
    }

    public String help() {
        return HELP;
    }

    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
