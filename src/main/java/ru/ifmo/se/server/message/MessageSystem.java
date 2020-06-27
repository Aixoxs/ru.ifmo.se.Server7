//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ru.ifmo.se.server.message;

import ru.ifmo.se.server.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessageSystem {
    private final Map<Class<?>, BlockingQueue<Message>> queues;

    public MessageSystem() {
        queues = new HashMap<>();
        queues.put(ru.ifmo.se.server.Controller.class, new ArrayBlockingQueue<>(15));
        queues.put(MessageWriter.class, new ArrayBlockingQueue<>(15));
    }

    public void putInQueues(Class<?> class1, Message message) {
        if (class1 == MessageReader.class) {
            this.queues.get(Controller.class).add(message);
        } else if (class1 == Controller.class) {
            this.queues.get(MessageWriter.class).add(message);
        }

    }

    public Object getFromQueues(Class<?> class1) throws InterruptedException {
        if (class1 == null) {
            throw new IllegalArgumentException();
        } else {
            return ((BlockingQueue)this.queues.get(class1)).take();
        }
    }

    public Map<Class<?>, BlockingQueue<Message>> getQueues() {
        return this.queues;
    }
}
