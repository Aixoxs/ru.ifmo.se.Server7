package ru.ifmo.se.server;

import ru.ifmo.se.commands.*;
import ru.ifmo.se.jdbc.*;
import ru.ifmo.se.manager.App;
import ru.ifmo.se.manager.Collection;
import ru.ifmo.se.model.User;
import ru.ifmo.se.server.message.Message;
import ru.ifmo.se.server.message.MessageSystem;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Invoker
 *
 * @author Gurin Minu
 * @version 0
 * @since 0
 */
public class Controller implements Runnable {
    private App app;
    private List<CommandName> hist = Collections.synchronizedList(new ArrayList<>());
    private Collection collection;
    private UserDAO dao;
    private ExecutorService service;
    private Thread controllerThread;
    private MessageSystem messageSystem;

    /**
     * Constructor Controller, который принимает команды
     */
    Controller(MessageSystem messageSystem, App app, Collection collection, UserDAO dao) {
        this.messageSystem = messageSystem;
        this.collection = collection;
        this.dao = dao;
        this.app = app;
        service = Executors.newFixedThreadPool(2);
    }

    /**
     * Начинает принимать команды пользователя
     */
    @Override
    public void run() {
        this.service.execute(this::execution);
    }

    void execution(){
        while (!Thread.currentThread().isInterrupted()) {
            Object objectResponse = null;
            Message message;
            try {
                message = (Message) messageSystem.getFromQueues(Controller.class);
            } catch (InterruptedException e) {
                continue;
            }
            Object object = message.getMessage();
            ClassCommand command;
            if (object instanceof User) {
                objectResponse = userAccess(object);
            } else if (object instanceof ClassCommand) {
                objectResponse = userAccess(((ClassCommand) object).getUser());
                if (objectResponse.equals("connect")) {
                    command = (ClassCommand) object;
                    if (command.getCommandName() == CommandName.HISTORY) {
                        objectResponse = history();
                    } else if (command.getCommandName() == CommandName.EXECUTE_SCRIPT) {
                        ArrayList<Object> resultArrayList = new ArrayList<>();
                        ExecuteScriptCommand executeScriptCommand = (ExecuteScriptCommand) command;
                        command.getUser().setPassword(hash("23&)$&2hd!#" + command.getUser().getPassword() + dao.readSalt(command.getUser())));
                        ((List<ClassCommand>) executeScriptCommand.getArgument()).forEach(classCommand -> {
                            classCommand.setUser(command.getUser());
                            if (classCommand.getCommandName() == CommandName.HISTORY) {
                                resultArrayList.add(history());
                            } else {
                                resultArrayList.add(classCommand.execute(new Context() {
                                    @Override
                                    public App app() {
                                        return app;
                                    }

                                    @Override
                                    public Collection collection() {
                                        return collection;
                                    }
                                }));
                            }
                            if (classCommand.getCommandName() != CommandName.ERROR) {
                                hist.add(classCommand.getCommandName());
                            }

                        });
                        objectResponse = resultArrayList;
                    } else {
                        System.out.println(command.getCommandName());
                        command.getUser().setPassword(hash("23&)$&2hd!#" + command.getUser().getPassword() + dao.readSalt(command.getUser())));
                        objectResponse = command.execute(new Context() {
                            @Override
                            public App app() {
                                return app;
                            }

                            @Override
                            public Collection collection() {
                                return collection;
                            }
                        });
                    }
                    hist.add(command.getCommandName());
                }
            }
            messageSystem.putInQueues(Controller.class, new Message(objectResponse, message.getReceiver()));
        }
    }

    private String userAccess(Object object){
        String objectResponse = "Ошибка авторизации";
        User objectUser = (User) object;
        User user = dao.read(objectUser.getLogin());
        String pepper = "23&)$&2hd!#";
        if (objectUser.getStatus() == User.Status.UNLOGIN) {
            if (user.getStatus() == User.Status.UNREGISTER) {
                objectResponse = "Пользователя с таким логином не существует";
            } else if (user.getPassword().equals(hash(pepper + objectUser.getPassword() + dao.readSalt(objectUser)))) {
                objectResponse = "connect";
            } else {
                objectResponse = "Пароль введен неправильно, повторите попытку:";
            }
        } else if (objectUser.getStatus() == User.Status.UNREGISTER) {
            String salt = randomString();
            objectUser.setPassword(hash(pepper + objectUser.getPassword() + salt));
            objectResponse = dao.create(objectUser, salt);
        }
        return objectResponse;
    }

    private String history() {
        StringBuilder result = new StringBuilder();
        if (hist.size() == 0) {
            result = new StringBuilder("Команд не найдено");
        } else if (hist.size() < 5) {
            for (CommandName command1 : hist) {
                result.append(command1.toString().toLowerCase()).append("\n");
            }
        } else {
            for (int i = hist.size() - 5; i < hist.size(); i++) {
                result.append(hist.get(i).toString().toLowerCase()).append("\n");
            }
        }
        return result.toString();
    }

    private String randomString() {
        Random r = new Random(); // perhaps make it a class variable so you don't make a new one every time
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            char c = (char) (r.nextInt(Character.MAX_VALUE));
            sb.append(c);
        }
        return sb.toString();
    }

    private String hash(String string) {
        StringBuilder hashtext = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(string.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            hashtext = new StringBuilder(no.toString(16));

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashtext.toString();
    }

    public void start() {
        this.controllerThread = new Thread(this, "controller_thread");
        this.controllerThread.setDaemon(true);
        this.controllerThread.start();
    }

    void disconnect() {
        System.out.println("Disconnecting the server...");

        try {
            this.service.shutdown();
            this.controllerThread.interrupt();
            this.service.shutdown();
            this.service.awaitTermination(500L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException var2) {
            System.out.println("Interrupted during finishing the queued tasks");
        }
    }
}
