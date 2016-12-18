package controller.tasks;

import controller.ErrorController;
import model.business.DataBusiness;
import model.business.UserBusiness;
import model.pojo.Data;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Guild;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class DailyLoginCheckup extends Thread {
    private JDA jda;

    public DailyLoginCheckup(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void run() {
        try {
            DataBusiness dataBusiness = new DataBusiness("wiki");
            Data lastLoginDate = dataBusiness.getByName("Daily Login");
            if(lastLoginDate == null) {
                lastLoginDate = new Data();
                lastLoginDate.setName("Daily Login");
                lastLoginDate.setDate(LocalDateTime.now());
                dataBusiness.add(lastLoginDate);
                renewUsersLogin();
            }
            else {
                LocalDateTime today = LocalDateTime.now();
                LocalDateTime lastLoginDay = lastLoginDate.getDate();
                if(today.getDayOfYear() == lastLoginDay.getDayOfYear() && today.getYear() == lastLoginDay.getYear()) {
                    renewUsersLogin();
                }
            }
        } catch (SQLException e) {
            ErrorController.logStackTrace(e);
        }
    }

    public void renewUsersLogin() {
        for(Guild guild : jda.getGuilds()) {
            try {
                UserBusiness userBusiness = new UserBusiness(guild.getId());
                userBusiness.renewLoginAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
