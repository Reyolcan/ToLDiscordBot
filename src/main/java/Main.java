import commands.Command;
import commands.PictureCommand;
import commands.StoneCommand;
import commands.UnitCommand;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import utils.CommandParser;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class Main extends ListenerAdapter {
    private static JDA jda;

    public static final CommandParser parser = new CommandParser();
    public static HashMap<String, Command> commands = new HashMap<String, Command>();
    public static HashMap<String, Command> adminCommands = new HashMap<String, Command>();

    public static void main(String[] args)
    {
        try
        {
            jda = new JDABuilder()
                    .addListener(new BotListener())
                    .setBotToken("MjE5NTI5NDgxNjA0MzAwODAw.CqT1zQ._PS7w0PXLNEBQsZc73MM5zTVHDo")
                    .buildBlocking();
            jda.setAutoReconnect(true);

            commands.put("dhaps", new PictureCommand());
            commands.put("units", new UnitCommand());
            commands.put("hs", new StoneCommand(false));

            adminCommands.put("hs", new StoneCommand(true));
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("The config was not populated. Please enter a bot token.");
        }
        catch (LoginException e)
        {
            System.out.println("The provided bot token was incorrect. Please provide valid details.");
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static void handleCommand(CommandParser.CommandContainer cmd) {
        if(commands.containsKey(cmd.invoke)) {
            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);

            if(safe) {
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
            else {

            }
        }
    }

    public static void handleAdminCommand(CommandParser.CommandContainer cmd) {
        if(adminCommands.containsKey(cmd.invoke)) {
            boolean safe = adminCommands.get(cmd.invoke).called(cmd.args, cmd.event);

            if(safe) {
                adminCommands.get(cmd.invoke).action(cmd.args, cmd.event);
                adminCommands.get(cmd.invoke).executed(safe, cmd.event);
            }
            else {

            }
        }
    }

}
