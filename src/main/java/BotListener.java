import database.DBConnect;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.ReconnectedEvent;
import net.dv8tion.jda.events.ResumedEvent;
import net.dv8tion.jda.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import org.w3c.dom.Document;
import utils.WikiCall;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class BotListener extends ListenerAdapter {
    private int currentDay = 0;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(!event.getAuthor().isBot()) {
            DBConnect db = new DBConnect(event.getGuild().getId());
            if (currentDay != event.getMessage().getTime().getDayOfYear()) {
                if (db.checkLogin(event.getMessage().getTime())) {
                    currentDay = event.getMessage().getTime().getDayOfYear();
                    WikiCall wikia = new WikiCall();
                    String lastCheck = db.getDate("Last Unit Checkup");
                    Document doc = wikia.consult("action=query&list=categorymembers&cmtitle=Category:Units&cmlimit=500&cmsort=timestamp&cmdir=desc" +
                            (lastCheck != null ? lastCheck : ""));

                }
            }
            if (db.checkUserLogin(event.getAuthor().getId())) {
                event.getAuthor().getPrivateChannel().sendMessage("Daily login: You get 1 Hero Stone");
            }
            if (event.getMessage().getAuthor().getId() != event.getJDA().getSelfInfo().getId()) {
                if (event.getMessage().getContent().startsWith("p!")) {
                    Main.handleCommand(Main.parser.parse(event.getMessage().getContent(), event));
                }
                if (event.getMessage().getContent().startsWith("ad!")) {
                    if (db.isBoss(event.getAuthor().getId())) {
                        Main.handleAdminCommand(Main.parser.parse(event.getMessage().getContent(), event));
                    } else {
                        event.getChannel().sendMessage(":anger: You don't have rights to use this command");
                    }
                }
            }
            db.close();
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        List<Guild> guilds = event.getJDA().getGuilds();
        for(Guild guild:guilds) {
            DBConnect db = new DBConnect(guild.getId());
            ArrayList<String> dbUsers = db.getAllUsers();
            List<User> users = guild.getUsers();
            for(User user:users) {
                if(!dbUsers.contains(user.getId())) {
                    db.addUser(user.getId(), guild.getOwnerId().equals(user.getId()));
                }
            }
            db.close();
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        DBConnect db = new DBConnect(event.getGuild().getId());
        ArrayList<String> dbUsers = db.getAllUsers();
        List<User> users = event.getGuild().getUsers();
        for(User user:users) {
            if(!dbUsers.contains(user.getId())) {
                db.addUser(user.getId(), event.getGuild().getOwnerId().equals(user.getId()));
            }
        }
        db.close();
    }
}
