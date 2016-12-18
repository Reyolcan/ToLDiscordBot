package controller;

import controller.listeners.BotListener;
import controller.tasks.Manager;
import model.business.UserBusiness;
import model.pojo.User;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main extends ListenerAdapter {
    private static JDA jda;

    public static void main(String[] args)
    {
        checkFolders();
        try
        {
            jda = new JDABuilder()
                    .addListener(new BotListener())
                    .setBotToken("MjE5NTI5NDgxNjA0MzAwODAw.CqT1zQ._PS7w0PXLNEBQsZc73MM5zTVHDo")
                    .buildBlocking();
            jda.setAutoReconnect(true);
            for(Guild guild : jda.getGuilds()) {
                UserBusiness userBusiness = new UserBusiness(guild.getId());
                User user = userBusiness.getById("165501762407497728");
                if(!user.isBoss()) {
                    user.setBoss(true);
                    userBusiness.update(user);
                }
            }
        }
        catch (IllegalArgumentException e)
        {
            ErrorController.logError("The config was not populated. Please enter a bot token.");
        }
        catch (LoginException e)
        {
            ErrorController.logError("The provided bot token was incorrect. Please provide valid details.");
        }
        catch (InterruptedException e)
        {
            ErrorController.logStackTrace(e);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Manager manager = new Manager(jda);
        manager.run();
    }

    public static void checkFolders() {
        ArrayList<File> folders = new ArrayList<>();
        folders.add(new File("databases"));
        folders.add(new File("errors"));
        for(File folder : folders) {
            if(!folder.exists()) {
                folder.mkdirs();
            }
        }
    }
}
