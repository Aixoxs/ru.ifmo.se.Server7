package ru.ifmo.se.server;

import ru.ifmo.se.jdbc.PostgreDB;
import ru.ifmo.se.jdbc.UserDAO;
import ru.ifmo.se.manager.App;
import ru.ifmo.se.manager.Collection;
import ru.ifmo.se.server.message.MessageReader;
import ru.ifmo.se.server.message.MessageSystem;
import ru.ifmo.se.server.message.MessageWriter;

import java.net.DatagramSocket;

class ClientSession {
    private DatagramSocket socket;
    private Controller controller;
    private MessageReader messageReader;
    private MessageWriter messageWriter;

    ClientSession(DatagramSocket socket) throws ClassNotFoundException {
        this.socket = socket;
        MessageSystem messageSystem = new MessageSystem();
        messageReader = new MessageReader(socket, messageSystem);
        messageWriter = new MessageWriter(messageSystem, socket);
        App app = new App();
        PostgreDB db = new PostgreDB();
        db.init();
        Collection collection = new Collection(db);
        UserDAO dao = new UserDAO(db.getConnection());
        controller = new Controller(messageSystem, app, collection, dao);
    }


    void run() {
        if (socket.isBound()) {
            messageReader.start();
            controller.start();
            messageWriter.start();
        }
    }

    void disconnect(){
        controller.disconnect();
        messageReader.stop();
        messageWriter.stop();
    }
}
