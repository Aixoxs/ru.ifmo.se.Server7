package ru.ifmo.se.commands;


import ru.ifmo.se.manager.Collection;

public class ClearCommand extends ClassCommand {
    public ClearCommand(){
        this.commandName = CommandName.CLEAR;
    }

    @Override
    public String execute(Context context) {
        return context.collection().clear(getUser());
    }
}
