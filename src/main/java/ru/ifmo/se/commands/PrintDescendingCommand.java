package ru.ifmo.se.commands;

import ru.ifmo.se.manager.Collection;

public class PrintDescendingCommand extends ClassCommand {
    public PrintDescendingCommand(){
        this.commandName = CommandName.PRINT_DESCENDING;
    }

    @Override
    public String execute(Context context) {
        return context.collection().printDescending();
    }
}