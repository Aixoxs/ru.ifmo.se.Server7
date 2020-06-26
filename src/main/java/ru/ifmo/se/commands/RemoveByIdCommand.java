package ru.ifmo.se.commands;

import ru.ifmo.se.manager.Collection;

public class RemoveByIdCommand extends ClassCommand {
    public RemoveByIdCommand(){
        this.commandName = CommandName.REMOVE_BY_ID;
    }

    @Override
    public String execute(Context context) {
        return context.collection().remove((Integer) this.getArgument(), getUser());
    }
}
