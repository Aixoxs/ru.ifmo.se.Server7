package ru.ifmo.se.commands;

import ru.ifmo.se.manager.Collection;

public class InfoCommand extends ClassCommand {
    public InfoCommand(){
        this.commandName = CommandName.INFO;
    }

    @Override
    public String execute(Context context) {
        return context.collection().info();
    }
}
