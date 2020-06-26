package ru.ifmo.se.server;

import ru.ifmo.se.manager.Collection;
import ru.ifmo.se.musicians.MusicBand;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.*;
import java.util.LinkedHashSet;
import java.util.concurrent.ExecutionException;

public class Server {
    private ServerData serverData;
    private DatagramSocket socket;
    private ClientSession clientSession;

    Server(int port) throws SocketException {
        this.serverData = new ServerData();
        this.socket = new DatagramSocket(new InetSocketAddress(port));
        clientSession = new ClientSession(socket);
    }

    void run() throws ExecutionException, InterruptedException {
        //Создается клиентская сессия
        this.serverData.getSessionsManger().addSession(clientSession);

        //Запуск логики работы с клиентом

        clientSession.run();
        socket.close();
    }

    void disconnect() {
        clientSession.disconnect();
        socket.disconnect();
    }
}
