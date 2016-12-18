package controller;

import model.CommandList;
import utils.Parser;

public class CommandController {
    public static void handleCommand(Parser.CommandContainer cmd) {
        for(CommandList commands : CommandList.values()) {
            if(commands.name().equals(cmd.invoke.toUpperCase())) {
                CommandList.valueOf(cmd.invoke.toUpperCase()).getCommand().action(cmd.args, cmd.event);
            }
        }
    }
}
