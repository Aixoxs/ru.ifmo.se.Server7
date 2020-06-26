package ru.ifmo.se.server;

import ru.ifmo.se.jdbc.PostgreDB;
import ru.ifmo.se.manager.Collection;

import java.net.SocketException;
import java.util.concurrent.ExecutionException;

public class ServerLauncher {
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            Server server = new Server(port);

            Runtime.getRuntime().addShutdownHook(new Thread(server::disconnect));

            server.run();
        } catch (SocketException var2) {
            var2.printStackTrace();
            System.exit(-1);
        } catch (IndexOutOfBoundsException var3) {
            System.out.println("Неправильно указаны аргументы(java -jar Server.jar filepath port)");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
