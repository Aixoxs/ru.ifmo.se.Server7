package ru.ifmo.se.commands;

import ru.ifmo.se.manager.App;

public class HelpCommand extends ClassCommand {
    public HelpCommand(){
        this.commandName = CommandName.HELP;
    }

    @Override
    public String execute(Context context) {
        return context.app().help();
    }
}
