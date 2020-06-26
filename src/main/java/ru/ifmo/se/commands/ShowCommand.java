package ru.ifmo.se.commands;

import ru.ifmo.se.manager.Collection;
import ru.ifmo.se.musicians.MusicBand;

import java.util.ArrayList;
import java.util.List;

public class ShowCommand extends ClassCommand {
    public ShowCommand(){
        this.commandName = CommandName.SHOW;
    }

    @Override
    public String execute(Context context) {
        return context.collection().show();
    }
}
