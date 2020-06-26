package ru.ifmo.se.commands;

import ru.ifmo.se.manager.Collection;
import ru.ifmo.se.musicians.MusicBand;

public class MaxByGenreCommand extends ClassCommand {
    public MaxByGenreCommand(){
        this.commandName = CommandName.MAX_BY_GENRE;
    }

    @Override
    public Object execute(Context context) {
        return context.collection().maxByGenre();
    }
}
