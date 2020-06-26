package ru.ifmo.se.server.message;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.concurrent.ForkJoinPool;

public class MessageWriter implements Runnable{
    private DatagramSocket socket;
    private MessageReader reader;
    private Object objectResponse;

    public MessageWriter(DatagramSocket socket, MessageReader reader, Object objectResponse) {
        this.socket = socket;
        this.reader = reader;
        this.objectResponse = objectResponse;
    }

    public void writeAnswer() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                try {
                    oos.writeObject(objectResponse);
                    oos.flush();
                } catch (IOException var5) {
                    var5.printStackTrace();
                } finally {
                    if (oos != null) {
                        baos.close();
                    }
                }

                // get the byte array of the object
                ByteBuffer Buf = ByteBuffer.wrap(baos.toByteArray());
                byte[] buf = new byte[Buf.remaining()];
                Buf.get(buf);

                /**int number = Buf.length;;
                 byte[] data = new byte[4];

                 // int -> byte[]
                 for (int i = 0; i < 4; ++i) {
                 int shift = i << 3; // i * 8
                 data[3-i] = (byte)((number & (0xff << shift)) >>> shift);
                 }



                 DatagramPacket packet = new DatagramPacket(data, 4, client, socket.getPort());
                 socket.send(packet);
                 **/
                // now send the payload
                DatagramPacket packet = new DatagramPacket(buf, buf.length, reader.getAddress());
                socket.send(packet);

                System.out.println("DONE SENDING");
            }catch (SocketException var7){
                MessageWriter o = new MessageWriter(this.socket, reader, "Файл слишком большой");
                o.run();
            }
            catch (IOException var6) {
                var6.printStackTrace();
            } finally {
                if (baos != null) {
                    baos.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        this.writeAnswer();
    }
}
