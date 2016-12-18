package controller.commands;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class AboutCommand implements Command {
    private final String HELP = "$$about - Displays information about the bot";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        event.getChannel().sendMessage("```PoringBot was created by Reyolcan for the Tales of Link Discord community.\n" +
                "If you notice any bugs or would like to make a suggestion, please contact him.\n" +
                "\n" +
                "Tales of Link characters info is taken directly from the Tales of Link Wikia, and may take some time to update. If you notice incorrect information about something from the Wikia, please let us know in the #wiki channel.\n" +
                "\n" +
                "Special Thanks\n" +
                "----\n" +
                "Arcelle, Caam, Ayleria, Namwin\n" +
                "\n" +
                "[Wiki]   http://tales-of-link.wikia.com\n" +
                "[Reddit] http://www.reddit.com/r/talesoflink```");
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
