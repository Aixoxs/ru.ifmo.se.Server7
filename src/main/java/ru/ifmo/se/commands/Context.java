package ru.ifmo.se.commands;

import ru.ifmo.se.manager.App;
import ru.ifmo.se.manager.Collection;

import java.io.Serializable;

public interface Context {
    App app();

    Collection collection();
}
