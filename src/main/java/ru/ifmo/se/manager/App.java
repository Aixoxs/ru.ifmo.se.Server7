package ru.ifmo.se.manager;

import ru.ifmo.se.musicians.MusicBand;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Receiver
 * Хранит исполнения команд
 */

public class App implements Serializable {
    /**
     * Constructor App
     */
    public App() {
    }

    /**
     * Выводит справку по доступным командам
     */
    public String  help() {
        return ("help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                "history : вывести последние 5 команд (без их аргументов)\n" +
                "max_by_genre : вывести любой объект из коллекции, значение поля genre которого является максимальным\n" +
                "filter_less_than_number_of_participants numberOfParticipants : вывести элементы, значение поля numberOfParticipants которых меньше заданного\n" +
                "print_descending : вывести элементы коллекции в порядке убывания");
    }
}
