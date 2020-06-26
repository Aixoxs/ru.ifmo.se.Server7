package ru.ifmo.se.server.message;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.Buffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MessageReader {
    private DatagramSocket socket;
    private SocketAddress address;
    private ExecutorService service = Executors.newFixedThreadPool(10);

    public MessageReader(DatagramSocket socket) {
        this.socket = socket;
    }

    public Object readCommand() throws InterruptedException, ExecutionException {
        Future result = this.service.submit(()-> {
            Object object = null;
            try {
                ByteBuffer buffer = ByteBuffer.allocate(1000000);
                this.getBuffer(buffer);
                ((Buffer) buffer).flip();
                byte[] petitionBytes = new byte[buffer.remaining()];
                buffer.get(petitionBytes);
                if (petitionBytes.length > 0) {
                    try (ByteArrayInputStream bais = new ByteArrayInputStream(petitionBytes)) {
                        try (ObjectInputStream oos = new ObjectInputStream(bais)) {
                            object = oos.readObject();
                        } catch (IOException var8) {
                            var8.printStackTrace();
                        }
                    } catch (IOException var7) {
                        var7.printStackTrace();
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
            return object;
        });
        return result.get();
    }

    private void getBuffer(ByteBuffer buffer) throws IOException {
        byte[] buf = new byte[buffer.remaining()];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        this.socket.receive(packet);
        address = packet.getSocketAddress();
        buffer.put(buf, 0, packet.getLength());
    }

    SocketAddress getAddress() {
        return address;
    }

    public void close(){
        service.shutdown();
    }
}
