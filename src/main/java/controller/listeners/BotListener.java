package controller.listeners;

import controller.CommandController;
import controller.ErrorController;
import model.business.UserBusiness;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import utils.Parser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BotListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(!event.getAuthor().isBot()) {
            try {
                UserBusiness userBusiness = new UserBusiness(event.getGuild().getId());
                model.pojo.User user = userBusiness.getById(event.getAuthor().getId());
                if (!user.isLogin()) {
                    user.setStones(user.getStones() + 5);
                    if(userBusiness.update(user) > 0) {
                        event.getAuthor().getPrivateChannel().sendMessage("Daily login: You get 5 Hero Stones");
                    }
                    user.setLogin(true);
                    userBusiness.update(user);
                }
                if (event.getMessage().getAuthor().getId() != event.getJDA().getSelfInfo().getId()) {
                    if(event.getMessage().getContent().startsWith("$$")) {
                        Parser parser = new Parser();
                        CommandController commandController = new CommandController();
                        commandController.handleCommand(parser.parseCommand(event.getMessage().getContent(), event));
                    }
                }
            } catch (SQLException e) {
                ErrorController.logStackTrace(e);
            }
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        try {
            List<Guild> guilds = event.getJDA().getGuilds();
            for(Guild guild:guilds) {
                UserBusiness userBusiness = new UserBusiness(guild.getId());
                ArrayList<model.pojo.User> dbUsers = userBusiness.getAll();
                ArrayList<String> dbUsersId = new ArrayList<>();
                for(model.pojo.User dbUser : dbUsers) {
                    dbUsersId.add(dbUser.getId());
                }
                List<User> users = guild.getUsers();
                for(User user:users) {
                    if(!dbUsersId.contains(user.getId())) {
                        model.pojo.User newUser = new model.pojo.User();
                        newUser.setId(user.getId());
                        newUser.setStones(0);
                        newUser.setLogin(false);
                        newUser.setBoss(false);
                        userBusiness.add(newUser);
                    }
                }
            }
        } catch (SQLException e) {
            ErrorController.logStackTrace(e);
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        try {
            UserBusiness userBusiness = new UserBusiness(event.getGuild().getId());
            model.pojo.User user = new model.pojo.User();
            user.setId(event.getUser().getId());
            user.setStones(0);
            user.setLogin(false);
            user.setBoss(false);
            userBusiness.add(user);
        } catch (SQLException e) {
            ErrorController.logStackTrace(e);
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        try {
            List<Guild> guilds = event.getJDA().getGuilds();
            for(Guild guild:guilds) {
                UserBusiness userBusiness = new UserBusiness(guild.getId());
                ArrayList<model.pojo.User> dbUsers = userBusiness.getAll();
                ArrayList<String> dbUsersId = new ArrayList<>();
                for(model.pojo.User dbUser : dbUsers) {
                    dbUsersId.add("" + dbUser.getId());
                }
                List<User> users = guild.getUsers();
                for(User user:users) {
                    if(!dbUsersId.contains(user.getId())) {
                        model.pojo.User newUser = new model.pojo.User();
                        newUser.setId(user.getId());
                        newUser.setStones(0);
                        newUser.setLogin(false);
                        newUser.setBoss(false);
                        userBusiness.add(newUser);
                    }
                }
            }
        } catch (SQLException e) {
            ErrorController.logStackTrace(e);
        }
    }
}
