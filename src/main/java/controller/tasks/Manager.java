package controller.tasks;

import net.dv8tion.jda.JDA;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Manager extends Thread {
    private JDA jda;

    public Manager(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void run() {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("Etc/Greenwich");
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        ZonedDateTime zonedNextDay = zonedNow.plusDays(1).withHour(0).withMinute(0).withSecond(0);
        /*zonedNext5 = zonedNow.withHour(5).withMinute(0).withSecond(0);
        if(zonedNow.compareTo(zonedNext5) > 0)
            zonedNext5 = zonedNext5.plusDays(1);*/

        Duration duration = Duration.between(zonedNow, zonedNextDay);
        long initalDelay = duration.getSeconds();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new DailyTasks(), initalDelay,
                24*60*60, TimeUnit.SECONDS);
    }

    public class DailyTasks extends Thread {
        @Override
        public void run() {
            new UpdateCharacterList().start();
            new DailyLoginCheckup(jda).start();
        }
    }
}