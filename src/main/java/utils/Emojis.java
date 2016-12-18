package utils;

import net.dv8tion.jda.entities.Emote;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;

public class Emojis {
    private static HashMap<String, HashMap<String, String>> guilds = new HashMap<>();

    public static String get(String name, MessageReceivedEvent event) {
        if(!guilds.containsKey(event.getGuild().getId())) {
            HashMap<String, String> emojis = new HashMap<>();
            emojis.put("5star", "[5☆]");
            emojis.put("4star", "[4☆]");
            emojis.put("3star", "[3☆]");
            List<Emote> guildEmojis = event.getGuild().getEmotes();
            for(Emote emoji : guildEmojis) {
                if(emojis.containsKey(emoji.getName())) {
                    emojis.put(emoji.getName(), emoji.getAsEmote());
                }
            }
            guilds.put(event.getGuild().getId(), emojis);
        }
        return guilds.get(event.getGuild().getId()).get(name);
    }
}
