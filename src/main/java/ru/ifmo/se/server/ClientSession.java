package ru.ifmo.se.server;

import ru.ifmo.se.jdbc.PostgreDB;
import ru.ifmo.se.jdbc.UserDAO;
import ru.ifmo.se.manager.App;
import ru.ifmo.se.manager.Collection;
import ru.ifmo.se.server.message.MessageReader;
import ru.ifmo.se.server.message.MessageWriter;

import java.net.DatagramSocket;
import java.util.Timer;
import java.util.concurrent.*;

class ClientSession {
    private DatagramSocket socket;
    private Controller controller;

    ClientSession(DatagramSocket socket) {
        this.socket = socket;
        MessageReader messageReader = new MessageReader(socket);
        App app = new App();
        PostgreDB db = new PostgreDB();
        Collection collection = new Collection(db);
        UserDAO dao = new UserDAO(db.getConnection());
        controller = new Controller(socket, app, messageReader, collection, dao);
    }


    void run() throws ExecutionException, InterruptedException {
        while (socket.isBound()) {
            controller.executeCommand();
        }
    }

    void disconnect(){
        controller.disconnect();
    }
}
