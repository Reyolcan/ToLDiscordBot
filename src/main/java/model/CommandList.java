package model;

import controller.commands.*;

public enum CommandList {
    ABOUT(new AboutCommand(), "Displays information about the bot", false),
    HELP(new HelpCommand(), "", false),
    PULL(new GachaCommand(), "Command for pulling in the gacha", false),
    HS(new StoneCommand(), "Command to check the amount of Hero Stones", false),
    UPDATE(new UpdateCommand(), "Updates character pool from the wiki", true);

    private final Command command;
    private final String help;
    private final boolean adminOnly;

    CommandList(Command command, String help, boolean adminOnly) {
        this.command = command;
        this.help = help;
        this.adminOnly = adminOnly;
    }

    public Command getCommand() {
        return this.command;
    }

    public String getHelp() {
        return help;
    }

    public boolean isAdminOnly() {
        return adminOnly;
    }
}
