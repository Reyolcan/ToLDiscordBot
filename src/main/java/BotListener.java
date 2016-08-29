import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMessage().getAuthor().getId() != event.getJDA().getSelfInfo().getId()) {
            if(event.getMessage().getContent().startsWith("p!")) {
                Main.handleCommand(Main.parser.parse(event.getMessage().getContent(), event));
            }
            if(event.getMessage().getContent().startsWith("ad!")) {
                Main.handleCommand(Main.parser.parse(event.getMessage().getContent(), event));
            }
        }
    }
}
