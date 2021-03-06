package utils;

import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class Parser {
    public CommandContainer parseCommand(String rw, MessageReceivedEvent event) {
        ArrayList<String> split = new ArrayList<String>();
        String raw = rw;
        if(event.getMessage().getMentionedUsers().size() > 0) {
            for(User user:event.getMessage().getMentionedUsers()) {
                raw = raw.replace(user.getUsername(), user.getUsername().replace(" ", "_"));
            }
        }
        String beheaded = raw.replaceFirst("\\$\\$", "");
        //String beheaded = raw.replaceFirst("(.*)! ", "");
        String[] splitBeheaded = beheaded.split(" ");
        for(String arg:splitBeheaded) {
            split.add(arg);
        }
        String invoke = split.get(0);
        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new CommandContainer(raw, beheaded, splitBeheaded, invoke, args, event);
    }

    public class CommandContainer {
        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final MessageReceivedEvent event;

        public CommandContainer(String raw, String beheaded, String[] splitBeheaded, String invoke, String[] args, MessageReceivedEvent event) {
            this.raw = raw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.event = event;
        }
    }
}
