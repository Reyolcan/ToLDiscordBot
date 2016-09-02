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

import java.util.ArrayList;
import java.util.List;

public class BotListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMessage().getAuthor().getId() != event.getJDA().getSelfInfo().getId()) {
            if(event.getMessage().getContent().startsWith("p!")) {
                Main.handleCommand(Main.parser.parse(event.getMessage().getContent(), event));
            }
            if(event.getMessage().getContent().startsWith("ad!")) {
                DBConnect db = new DBConnect(event.getGuild().getId());
                if(db.isBoss(event.getAuthor().getId())) {
                    db.close();
                    Main.handleAdminCommand(Main.parser.parse(event.getMessage().getContent(), event));
                }
                else {
                    db.close();
                    event.getChannel().sendMessage(":anger: You don't have rights to use this command");
                }
            }
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
