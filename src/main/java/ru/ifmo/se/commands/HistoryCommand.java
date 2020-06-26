package ru.ifmo.se.commands;

import ru.ifmo.se.manager.App;

public class HistoryCommand extends ClassCommand {
    public HistoryCommand(){
        this.commandName = CommandName.HISTORY;
    }

    @Override
    public Object execute(Context context) {
        return new Object();
    }
}
